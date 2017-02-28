package fernandoperez.lifemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.utils.constants;

import static fernandoperez.lifemanager.utils.constants.DISPLAY_LOW;

public class AddConfigurationActivity extends AppCompatActivity {

    // Services.
    ImageButton imageButtonTwitter; boolean tw = false;
    ImageButton imageButtonSpotify; boolean sp = false;
    ImageButton imageButtonGmail; boolean gm = false;
    ImageButton imageButtonBluetooth; boolean bl = false;
    ImageButton imageButtonWifi; boolean wi = false;
    ImageButton imageButtonGPS; boolean gp = false;

    // Configuration
    EditText configname;
    private String confName;

    // Database management.
    private DaoSession daoSession;
    private ConfigurationsDao configurationsDao;

    private Context mContext;
    private ActionBar mActionBar;
    private GridLayout mGridLayout;
    private float mDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addconfig);

        mContext = this;

        // Database managament.
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();

        loadButtons();

        Intent intent = getIntent();
        confName = intent.getStringExtra(constants.CONFIGURATION_NAME);

        mGridLayout = (GridLayout) findViewById(R.id.gridlayout_addconfig);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (dm.densityDpi == DISPLAY_LOW){
            mGridLayout.setRowCount(3);
            mGridLayout.setColumnCount(2);
        }

        try {
            mActionBar =  getSupportActionBar();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (confName != null && !confName.isEmpty()) {
            if (mActionBar != null){
                mActionBar.setTitle("Editing: " + confName);
            }
            loadServices();
        }
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.action_backup:
                intent = new Intent(this, BackUpActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_aboutus:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     */
    private void loadServices() {
        List<Services> services;
        Configurations configuration = configurationsDao.queryBuilder()
          .where(ConfigurationsDao.Properties.Name.eq(confName))
          .unique();

        if (configuration == null) {
            return;
        }

        configname.setText(confName);

        services = configuration.getArrivingServicesList();
        for(Iterator<Services> iterator = services.iterator(); iterator.hasNext();){
            Services service = iterator.next();
            if(service.getName().equals("Twitter")) imageButtonTwitter.performClick();
            if(service.getName().equals("Spotify")) imageButtonSpotify.performClick();
            if(service.getName().equals("Gmail")) imageButtonGmail.performClick();
            if(service.getName().equals("Bluetooth")) imageButtonBluetooth.performClick();
            if(service.getName().equals("Wi-Fi"))imageButtonWifi.performClick();
            if(service.getName().equals("GPS")) imageButtonGPS.performClick();
        }
    }

    /**
     *
     */
    private void loadButtons() {
        //Buttons Declaration
        imageButtonTwitter = (ImageButton) findViewById(R.id.imB1);
        imageButtonSpotify = (ImageButton) findViewById(R.id.imB2);
        imageButtonGmail = (ImageButton) findViewById(R.id.imB3);
        imageButtonBluetooth = (ImageButton) findViewById(R.id.imB4);
        imageButtonWifi = (ImageButton) findViewById(R.id.imB5);
        imageButtonGPS = (ImageButton) findViewById(R.id.imB6);

        configname = (EditText) findViewById(R.id.edittext_activity_main);

        imageButtonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonTwitter.setSelected(!imageButtonTwitter.isSelected());
                if (imageButtonTwitter.isSelected()) {
                    //Handle selected state change
                    imageButtonTwitter.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitter));
                    tw=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonTwitter.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitterg));
                    tw=false;
                }
            }
        });

        imageButtonSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonSpotify.setSelected(!imageButtonSpotify.isSelected());
                if (imageButtonSpotify.isSelected()) {

                    imageButtonSpotify.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spoti));
                    sp=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonSpotify.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spotig));
                    sp=false;
                }


            }

        });

        imageButtonGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonGmail.setSelected(!imageButtonGmail.isSelected());
                if (imageButtonGmail.isSelected()) {
                    //Handle selected state change
                    //imageButtonTwitter = (ImageButton) setFeatureDrawable();

                    imageButtonGmail.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmail));
                    gm=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonGmail.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmailg));
                    gm = false;
                }


            }

        });

        imageButtonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonBluetooth.setSelected(!imageButtonBluetooth.isSelected());
                if (imageButtonBluetooth.isSelected()) {
                    imageButtonBluetooth.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.bluetooth));
                    bl=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonBluetooth.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.blg));
                    bl=false;
                }


            }

        });

        imageButtonWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonWifi.setSelected(!imageButtonWifi.isSelected());
                if (imageButtonWifi.isSelected()) {
                    imageButtonWifi.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifi));
                    wi=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonWifi.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifig));
                    wi=false;
                }
            }
        });

        imageButtonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonGPS.setSelected(!imageButtonGPS.isSelected());
                if (imageButtonGPS.isSelected()) {
                    imageButtonGPS.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gps));
                    gp=true;
                }
                else {
                    //Handle de-select state change
                    imageButtonGPS.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gpsg));
                    gp=false;
                }
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confName = configname.getText().toString();
                List<String> servicesToAdd = new ArrayList<>();

                if (confName.isEmpty()) {
                    Toast.makeText(mContext, "Configuration name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(tw) servicesToAdd.add("Twitter");
                if(sp) servicesToAdd.add("Spotify");
                if(gm) servicesToAdd.add("Gmail");
                if(bl) servicesToAdd.add("Bluetooth");
                if(wi) servicesToAdd.add("Wi-Fi");
                if(gp) servicesToAdd.add("GPS");

                Configurations saved = DBHelper.saveNewConfiguration(mContext, daoSession, confName, servicesToAdd);
                if (saved != null) {
                    Toast.makeText(mContext, "Configuration with services saved successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


}
