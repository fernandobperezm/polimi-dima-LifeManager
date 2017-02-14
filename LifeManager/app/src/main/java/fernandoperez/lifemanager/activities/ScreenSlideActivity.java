package fernandoperez.lifemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.googleapi.fragments.GmailFragment;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.spotifyapi.fragments.SpotifyPlaybackFragment;
import fernandoperez.lifemanager.twitterapi.fragments.TwitterMainFragment;
import fernandoperez.lifemanager.utils.constants;

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
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = constants.MAX_SERVICES;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        // TODO: mListServices should be retrieved from the config.
        List<Services> servicesList = new ArrayList<>();

        servicesList.add(new Services("Twitter", "Twitter Inc.", constants.SERVICES_LIST.TWITTER));
        servicesList.add(new Services("Spotify","Spotify Inc.", constants.SERVICES_LIST.SPOTIFY));
        servicesList.add(new Services("Wifi","Spotify Inc.", constants.SERVICES_LIST.EMAIL));

        Collections.sort(servicesList);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), servicesList);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("Scrolled " + String.valueOf(position));
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
                System.out.println("SCROLLSTATECHANGED " +  String.valueOf(state));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // Navigate "up" the demo structure to the launchpad activity.
//                // See http://developer.android.com/design/patterns/navigation.html for more.
//                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
//                return true;
//
//        }

        return super.onOptionsItemSelected(item);
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


    /**
     * A simple pager adapter that represents 5 {@link } objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<Services> mServicesList;
        FragmentManager supportFragmentManager;

        private ScreenSlidePagerAdapter(FragmentManager fm, List<Services> servicesList) {
            super(fm);
            this.supportFragmentManager = fm;
            this.mServicesList = servicesList;
        }

        @Override
        public Fragment getItem(int position) {
            switch (mServicesList.get(position).getEnum()) {
                case SPOTIFY:
                    // Spotify handles by itself the login.
                    return SpotifyPlaybackFragment.create();

                case TWITTER:
                    // Twitter only sets the session on the Session Manager, we must retrieve it
                    return TwitterMainFragment.create();

                case EMAIL:
                    return GmailFragment.create();

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
