package fernandoperez.lifemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.constants.constants;
import fernandoperez.lifemanager.googleapi.CreateFileInAppFolderActivity;

/**
 * BackUpActivity holds the preferences of the user for backing up the data of the app.
 */
public class BackUpActivity extends AppCompatActivity  {

    static boolean isGoogleDriveBackupOn = false;
    static boolean isLocalBackupON = false;

    private static final String TAG = "LifeManager";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manageSwitchButton();
    }

    /**
     * manageSwitchButton is a method that handles the backup ON/OFF switches.
     */
    private void manageSwitchButton(){
        // We need to have the relative layout we want to turn on with the switch. It must be final
        // in order to be visible inside the onCheckecChanged Method.
        final RelativeLayout googleDriveBackupLayout = (RelativeLayout)
                findViewById(R.id.relativelayout_settings_backup_googledrivebackupONlayout);

        final RelativeLayout localBackupLayout = (RelativeLayout)
                findViewById(R.id.relativelayout_settings_backup_localbackupONlayout);

        // This is the switch for turning on/off the backup for both local and google drive.
        // The method setOnCheckedChangeListener implements a listener object that checks if the
        // switch is activated or not, when it gets activated we turn on the visibility of the backup
        // config, otherwise, we turn it off.
        Switch googleDriveBackupSwitch = (Switch) findViewById(R.id.switch_settings_backup_googledriveswitchbackup);
        googleDriveBackupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    googleDriveBackupLayout.setVisibility(View.VISIBLE);
                    isGoogleDriveBackupOn = true;
                    System.out.println("GD ON");
                } else {
                    googleDriveBackupLayout.setVisibility(View.GONE);
                    isGoogleDriveBackupOn = false;
                    System.out.println("GD OFF");
                }
            }
        });

        Switch localBackupSwitch = (Switch) findViewById(R.id.switch_settings_backup_localswitchbackup);
        localBackupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    localBackupLayout.setVisibility(View.VISIBLE);
                    isLocalBackupON = true;
                    System.out.println("LOCAL ON");
                } else {
                    localBackupLayout.setVisibility(View.GONE);
                    isLocalBackupON = false;
                    System.out.println("LOCAL OFF");
                }
            }
        });
    }

    /**
     *  The pickGoogleDriveFolder methods starts, provided by Google(R).
     */
    public void pickGoogleDriveFolder(View view) {
        // TODO: make the google drive creation of file in AppFolder work.
//        Intent intent = new Intent(this, CreateFileInAppFolderActivity.class);
//        startActivity(intent);
    }

    /**
     * This method does a full backup of the app content both in google drive and local if they're
     * turned on.
     * @param view
     */
    public void makeBackUpNow(View view) {
        //TODO: make the backup in google drive and local.
    }

    /**
     * Method called when CreateFolderActivity finishes creating the folder.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == constants.CREATE_GOOGLEDRIVE_BACKUP_FOLDER) {
            // Make sure the request was successful
            if (resultCode == constants.CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_OK) {
                // TODO: Make the button unclickable.
                String backupFolderName = data.getDataString();
                System.out.println(backupFolderName);
            } else if (resultCode == constants.CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_ALREADY_CREATED) {
                // TODO: Deactivate the button.
                System.out.println("NO CREATED.");
            }
        }
    }
}
