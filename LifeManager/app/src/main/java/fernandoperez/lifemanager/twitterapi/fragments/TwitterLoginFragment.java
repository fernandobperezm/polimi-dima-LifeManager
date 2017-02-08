package fernandoperez.lifemanager.twitterapi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.fragments.SpotifySlidePageFragment;


public class TwitterLoginFragment extends Fragment {
    private TwitterLoginButton loginButton;

    public static TwitterLoginFragment create() {
        TwitterLoginFragment fragment = new TwitterLoginFragment();
        return fragment;
    }

    public TwitterLoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_twitter_main_login, container, false);


        createLoginButton(rootView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /**
     * The createLoginButton method finds the twitter login button and creates a callback for it,
     * this callback manages the action of the button after a login attempt is successful or not.
     * If the login attempt is successful, it start the activity to show the EmbeddedTimeLine.
     */
    protected void createLoginButton(ViewGroup container){
        loginButton = (TwitterLoginButton) container.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
//                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                // As the user could log in, we will show the timeline.
//                Intent intent = new Intent(TwitterLoginFragment.this, TwitterEmbeddedTimelineFragment.class);
//                startActivity(intent);
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
