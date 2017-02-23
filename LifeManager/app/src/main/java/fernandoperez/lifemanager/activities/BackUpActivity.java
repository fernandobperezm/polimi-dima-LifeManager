package fernandoperez.lifemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.ArrivingConfWithServDao;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.LeavingConfWithServDao;
import fernandoperez.lifemanager.models.ServicesDao;
import fernandoperez.lifemanager.utils.constants;

/**
 * BackUpActivity holds the preferences of the user for backing up the data of the app.
 */
public class BackUpActivity extends AppCompatActivity  {

    static boolean isGoogleDriveBackupOn = false;
    static boolean isLocalBackupON = false;

    private static final String TAG = "LifeManager";
    private Context mContext;

    private DaoSession daoSession;
    private ConfigurationsDao configurationsDao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manageSwitchButton();
        mContext = getApplicationContext();
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();
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

        makeLocalBackup();
    }

    /**
     *
     */
    public void makeLocalBackup() {
        StringWriter writer = new StringWriter();
        XmlSerializer xmlSerializer = Xml.newSerializer();

        try {
            FileOutputStream fileos = mContext.openFileOutput(constants.BACKUP_FILENAME, Context.MODE_PRIVATE);
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "Backup");

            List<Configurations> backupConfigurations = DBHelper.getAllConfigurations(configurationsDao);
            for (Iterator<Configurations> iterator = backupConfigurations.iterator(); iterator.hasNext(); ){
                Configurations conf = iterator.next();
                conf.toXML(xmlSerializer);
            }

            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        readLocalBackUp();
    }

    private void readLocalBackUp() {
        // TODO: read the backup as XML and load the data.

        FileInputStream inputStream;
        InputStreamReader inputStreamReader;

        try {
            inputStream = openFileInput(constants.BACKUP_FILENAME);
            inputStreamReader = new InputStreamReader(inputStream);
            char[] inputBuffer = new char[inputStream.available()];
            inputStreamReader.read(inputBuffer);
            String data = new String (inputBuffer);
            inputStream.close();
            inputStreamReader.close();
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
