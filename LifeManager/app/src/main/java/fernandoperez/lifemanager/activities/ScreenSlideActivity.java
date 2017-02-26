package fernandoperez.lifemanager.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.googleapi.fragments.GmailFragment;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.spotifyapi.fragments.SpotifyPlaybackFragment;
import fernandoperez.lifemanager.twitterapi.fragments.TwitterMainFragment;
import fernandoperez.lifemanager.utils.Tuple;
import fernandoperez.lifemanager.utils.constants;

import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.BLUETOOTH;
import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.EMAIL;
import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.LOCATION;
import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.SPOTIFY;
import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.TWITTER;
import static fernandoperez.lifemanager.utils.constants.SERVICES_LIST.WIFI;


/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 */
public class ScreenSlideActivity extends FragmentActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    /**
     * WIFI and Bluetooth variables.
     */
    WifiManager wifiManager;
    BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Intent intent = getIntent();
        String confName = intent.getExtras().getString(constants.CONFIGURATION_NAME);

        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        ConfigurationsDao configurationsDao = daoSession.getConfigurationsDao();

        Configurations currentConfiguration =
          configurationsDao.queryBuilder().where(ConfigurationsDao.Properties.Name.eq(confName)).unique();

        List<Services> servicesList = currentConfiguration.getArrivingServicesList();
        Tuple<List<Services>, List<Services>> tuple = extractServices(servicesList);

        // Activate the internal services, located in the first position of the tuple.
        if (!tuple.first.isEmpty()) {
            activateInternalServices(tuple.first);
        }

        // If there aren't external services to show, make a toast and go to the previous activity.
        if (tuple.second.isEmpty()) {
            Toast.makeText(this, "No external services to show, selected internal services are activated.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), tuple.second);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int fragmentPosition) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment fragment = fragmentManager.getFragments().get(fragmentPosition);

                if (fragment instanceof TwitterMainFragment) {
                    ((TwitterMainFragment) fragment).fetchData();
                }

                if (fragment instanceof  SpotifyPlaybackFragment) {
                    ((SpotifyPlaybackFragment) fragment).fetchData();
                }

                if (fragment instanceof GmailFragment) {
                    ((GmailFragment) fragment).fetchData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * The method onActivityResult is called after the
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        int fragmentPosition = mPager.getCurrentItem();
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(fragmentPosition);

        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
            mPagerAdapter.notifyDataSetChanged();
        }
        else Log.d("ScreenSlideActivity", "fragment is null");
    }

    private void activateInternalServices(List<Services> servicesList) {
        for (Services service : servicesList) {
            switch (service.getServiceType()) {
                case WIFI:
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    }
                    break;

                case BLUETOOTH:
                    if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                        mBluetoothAdapter.enable();
                    }
                    break;

                case LOCATION:
                    Intent gpsOptionsIntent = new Intent(
                      android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     *
     * @param servicesList
     * @return
     */
    private Tuple<List<Services>, List<Services>> extractServices(List<Services> servicesList) {
        List<Services> internalList = new ArrayList<>();
        List<Services> externalList = new ArrayList<>();

        for (Services service : servicesList) {
            constants.SERVICES_LIST servType = service.getServiceType();
            if (servType == WIFI || servType == BLUETOOTH || servType == LOCATION) {
                internalList.add(service);
            }

            if (servType == TWITTER || servType == EMAIL || servType == SPOTIFY) {
                externalList.add(service);
            }
        }

        return new Tuple<>(internalList, externalList);
    }


    /**
     * A simple pager adapter that represents 5 {@link } objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        List<Services> mServicesList;
        FragmentManager supportFragmentManager;

        private ScreenSlidePagerAdapter(FragmentManager fm, List<Services> servicesList) {
            super(fm);
            this.supportFragmentManager = fm;
            this.mServicesList = servicesList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            boolean isFirst = position == 0;
            switch (mServicesList.get(position).getServiceType()) {
                case SPOTIFY:
                    // Spotify handles by itself the login.
                    fragment = SpotifyPlaybackFragment.create(isFirst);
                    return fragment;

                case TWITTER:
                    // As fetching the data is made inside the fragment after login, we don't need
                    // to specify if this fragment is the first.
                    fragment =  TwitterMainFragment.create();
                    return fragment;

                case EMAIL:
                    fragment =  GmailFragment.create(isFirst);
                    return fragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mServicesList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentPagerAdapter.POSITION_UNCHANGED;
        }
    }
}
