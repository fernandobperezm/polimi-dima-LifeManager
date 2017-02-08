package fernandoperez.lifemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.utils.constants;
import fernandoperez.lifemanager.spotifyapi.SpotifyMainActivity;
import fernandoperez.lifemanager.twitterapi.activities.TwitterLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_twitter_login:
                intent = new Intent(this, TwitterLoginActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_spotify_login:
                intent = new Intent(this, SpotifyMainActivity.class);
                intent.putExtra(constants.SPOTIFIY_INDEX_INTENT,2);
                startActivity(intent);

                return true;

            case R.id.action_current_settings:
                intent = new Intent(this, ScreenSlideActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
