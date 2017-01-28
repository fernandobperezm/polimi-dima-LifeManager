package fernandoperez.lifemanager.twitterapi;

import android.app.ListActivity;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;
import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.constants.constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmbeddedTimelineActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_timeline);

        // TODO: Handle the session better, if there's no active session we should make the user
        // to login.
        TwitterSession session = Twitter.getSessionManager().getActiveSession();

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
                final List<Tweet> tweetList = response.body();

                final FixedTweetTimeline userTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(tweetList)
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter
                        .Builder(EmbeddedTimelineActivity.this)
                            .setTimeline(userTimeline)
                            .build();

                setListAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                //TODO: Handle this error in a better way.
                System.out.println(t);
            }
        });
    }
}
