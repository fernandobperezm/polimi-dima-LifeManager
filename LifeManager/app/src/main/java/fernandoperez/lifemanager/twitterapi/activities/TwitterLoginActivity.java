package fernandoperez.lifemanager.twitterapi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;

import fernandoperez.lifemanager.R;


public class TwitterLoginActivity extends AppCompatActivity {
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);

        createLoginButton();

    }

    /**
     * The createLoginButton method finds the twitter login button and creates a callback for it,
     * this callback manages the action of the button after a login attempt is successful or not.
     * If the login attempt is successful, it start the activity to show the EmbeddedTimeLine.
     */
    protected void createLoginButton(){
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
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
                Intent intent = new Intent(TwitterLoginActivity.this, EmbeddedTimelineActivity.class);
                startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }




}
