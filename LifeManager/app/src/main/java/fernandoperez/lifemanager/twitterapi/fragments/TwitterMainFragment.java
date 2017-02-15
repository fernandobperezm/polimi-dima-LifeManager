package fernandoperez.lifemanager.twitterapi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.utils.constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TwitterMainFragment extends ListFragment {
    private TwitterLoginButton loginButton;
    private ViewGroup mRootView;

    private List<Tweet> mTweetList;
    private FixedTweetTimeline mUserTimeline;
    private TweetTimelineListAdapter mTimelineAdapter;

    public TwitterMainFragment() {
    }

    public static TwitterMainFragment create() {
        return new TwitterMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =
          (ViewGroup) inflater.inflate(R.layout.fragment_twitter_main, container, false);

        TwitterSession twitterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();

        if (twitterSession == null) {
            inflateLoginButton();
        } else {
            inflateTimeline();
        }

        return mRootView;
    }

    protected void inflateLoginButton() {
        mRootView.findViewById(R.id.relativelayout_twitter_login).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.linearlayout_twitter_timeline).setVisibility(View.GONE);
        createLoginButton(mRootView);
    }

    protected void inflateTimeline() {
        mRootView.findViewById(R.id.relativelayout_twitter_login).setVisibility(View.GONE);
        mRootView.findViewById(R.id.linearlayout_twitter_timeline).setVisibility(View.VISIBLE);
        fetchData();
    }

    public void fetchData() {
        Twitter.getApiClient()
          .getStatusesService()
          .homeTimeline(
            constants.TWITTER_MAX_HOME_TIMELINE_TWEETS,
            null,
            null,
            null,
            null,
            null,
            null)
          .enqueue(new Callback<List<Tweet>>() {
              @Override
              public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                  //TODO: Make a better handling of errors.
                  if (!response.isSuccessful()) {
                      System.out.println("Not successful.");
                      return ;
                  }

                  mTweetList = response.body();

                  mUserTimeline = new FixedTweetTimeline.Builder()
                    .setTweets(mTweetList)
                    .build();

                  mTimelineAdapter = new TweetTimelineListAdapter
                    .Builder(getContext())
                    .setTimeline(mUserTimeline)
                    .build();

                  setListAdapter(mTimelineAdapter);
              }

              @Override
              public void onFailure(Call<List<Tweet>> call, Throwable t) {
                  //TODO: Handle this error in a better way.
                  System.out.println(t);
              }
          });
    }

    /**
     * The createLoginButton method finds the twitter login button and creates a callback for it,
     * this callback manages the action of the button after a login attempt is successful or not.
     * If the login attempt is successful, it start the activity to show the EmbeddedTimeLine.
     */
    protected void createLoginButton(final ViewGroup container){
        loginButton = (TwitterLoginButton) container.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("TwitterKit", "Login with Twitter success");
                inflateTimeline();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
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
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
