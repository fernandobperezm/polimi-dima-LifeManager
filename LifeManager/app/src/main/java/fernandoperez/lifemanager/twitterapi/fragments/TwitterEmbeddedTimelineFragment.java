package fernandoperez.lifemanager.twitterapi.fragments;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.utils.constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TwitterEmbeddedTimelineFragment extends ListFragment {

    public TwitterEmbeddedTimelineFragment() {
    }

    public static TwitterEmbeddedTimelineFragment create() {
        TwitterEmbeddedTimelineFragment fragment = new TwitterEmbeddedTimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup) inflater.inflate(R.layout.fragment_twitter_main_embeddedtimeline, container, false);

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
                                .Builder(getContext())
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

        return rootView;
    }
}
