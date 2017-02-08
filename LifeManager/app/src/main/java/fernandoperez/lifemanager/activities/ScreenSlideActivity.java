package fernandoperez.lifemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.fragments.ScreenSlidePageFragment;
import fernandoperez.lifemanager.fragments.SpotifySlidePageFragment;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.twitterapi.fragments.TwitterEmbeddedTimelineFragment;
import fernandoperez.lifemanager.twitterapi.fragments.TwitterLoginFragment;
import fernandoperez.lifemanager.utils.constants;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends FragmentActivity {
    /**
     *
     */
    TwitterSession twitterSession;

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

        // Twitter Session Manager.
        twitterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();

        // TODO: mListServices should be retrieved from the config.
        List<Services> servicesList = new ArrayList<Services>();
        servicesList.add(new Services("Spotify","Spotify Inc.", constants.SERVICES_LIST.SPOTIFY));
        servicesList.add(new Services("Twitter", "Twitter Inc.", constants.SERVICES_LIST.TWITTER));
        servicesList.add(new Services("Wifi","Spotify Inc.", constants.SERVICES_LIST.SPOTIFY));
        servicesList.add(new Services("GPS", "Twitter Inc.", constants.SERVICES_LIST.TWITTER));
        servicesList.add(new Services("Weather","Spotify Inc.", constants.SERVICES_LIST.TWITTER));
        servicesList.add(new Services("Facebook", "Twitter Inc.", constants.SERVICES_LIST.SPOTIFY));

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), servicesList);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.removeOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        // TODO: this needs to find the twitter fragment.
        // TODO: http://stackoverflow.com/questions/12384971/android-fragmentstatepageradapter-how-to-tag-a-fragment-to-find-it-later

        int fragmentPosition = mPager.getCurrentItem();
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(fragmentPosition);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
            mPagerAdapter.notifyDataSetChanged();
        }
        else Log.d("Twitter", "fragment is null");
    }



    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<Services> mServicesList;
        FragmentManager supportFragmentManager;
        public HashMap<Integer, Fragment> hola;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Services> servicesList) {
            super(fm);
            this.supportFragmentManager = fm;
            this.mServicesList = servicesList;
        }

        @Override
        public Fragment getItem(int position) {
            switch (mServicesList.get(position).getEnum()) {
                case SPOTIFY:
                    return SpotifySlidePageFragment.create();

                case TWITTER:
                    if (twitterSession == null) {
                        return TwitterLoginFragment.create();
                    } else {
                        return TwitterEmbeddedTimelineFragment.create();
                    }


                default:
                    return SpotifySlidePageFragment.create();
            }
        }

        @Override
        public int getCount() {
            return mServicesList.size();
        }
    }


}
