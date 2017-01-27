package fernandoperez.lifemanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fernandoperez.lifemanager.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /**
     * This method is implemented as the callback for the Backup Button when it's clicked.
     * When it's clicked, it must ask the user if they want to do a back up now if the settings are done,
     * otherwise, configure the google drive access and do a backup.
     * @param view The current view clicked
     */
    public void askForBackup(View view) {
        Intent intent = new Intent(this, BackUpActivity.class);
        startActivity(intent);
    }

    /**
     * This method is implemented as the callback for the About us Button when it's clicked.
     * When it's clicked, it must start the activity "About us".
     * @param view The current view clicked
     */
    public void startAboutUs(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }
}
