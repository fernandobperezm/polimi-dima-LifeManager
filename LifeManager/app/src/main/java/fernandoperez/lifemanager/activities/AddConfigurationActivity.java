package fernandoperez.lifemanager.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.ArrivingConfWithServDao;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.LeavingConfWithServDao;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.models.ServicesDao;

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

    // WIFI parameters declaration
    WifiManager wifiManager;
    BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;

    // Database management.
    private DaoSession daoSession;
    private ConfigurationsDao configurationsDao;
    private ServicesDao servicesDao;
    private ArrivingConfWithServDao arrivingDao;
    private LeavingConfWithServDao leavingDao;

    private Integer counter = 0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // Database managament.
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();
        servicesDao = daoSession.getServicesDao();
        arrivingDao = daoSession.getArrivingConfWithServDao();
        leavingDao = daoSession.getLeavingConfWithServDao();

        // Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Buttons Declaration
        imageButtonTwitter =(ImageButton) findViewById(R.id.imB1);
        imageButtonSpotify =(ImageButton) findViewById(R.id.imB2);
        imageButtonGmail =(ImageButton) findViewById(R.id.imB3);
        imageButtonBluetooth =(ImageButton) findViewById(R.id.imB4);
        imageButtonWifi =(ImageButton) findViewById(R.id.imB5);
        imageButtonGPS =(ImageButton) findViewById(R.id.imB6);

        wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
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
                    //Handle selected state change
                    //imageButtonTwitter = (ImageButton) setFeatureDrawable();

                    imageButtonBluetooth.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.bluetooth));

                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        bl=true;
                    }
                }
                else {
                    //Handle de-select state change
                    imageButtonBluetooth.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.blg));
                    mBluetoothAdapter.disable();
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
                    if(wifiManager.isWifiEnabled()==false){
                        wifiManager.setWifiEnabled(true);
                        Toast.makeText(AddConfigurationActivity.this, "wifi on",Toast.LENGTH_SHORT).show();
                        wi=true;
                    }
                }
                else {
                    //Handle de-select state change
                    imageButtonWifi.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifig));
                    if(wifiManager.isWifiEnabled()==true){
                        wifiManager.setWifiEnabled(false);
                        wi=false;
                    }
                }


            }

        });

        imageButtonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonGPS.setSelected(!imageButtonGPS.isSelected());
                if (imageButtonGPS.isSelected()) {

                    imageButtonGPS.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gps));
                    Intent gpsOptionsIntent = new Intent(
                      android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
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
                }
            }
        });

        Intent intent = getIntent();
        confName = intent.getStringExtra("CONF_NAME");

        if (!confName.isEmpty()) {
            loadServices();
        }
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

            case R.id.action_current_settings:
                intent = new Intent(this, ScreenSlideActivity.class);


                return true;

            case R.id.action_create_config:
                counter += 1;
                confName = "Work" + counter.toString();

                Configurations configuration = new Configurations(null, confName);
                DBHelper.insertConfiguration(this, configurationsDao, configuration);

                return true;

            case R.id.action_create_services:
                // Example of services adding to arriving and leaving.
                confName = "Work" + counter.toString();
                List<String> servicesToAdd = new ArrayList<>();
                servicesToAdd.add("Gmail");
                servicesToAdd.add("Twitter");
                servicesToAdd.add("Spotify");

                Configurations conf1 =
                  DBHelper
                    .insertArrivingServices(
                      this,
                      configurationsDao,
                      servicesDao,
                      arrivingDao,
                      confName,
                      servicesToAdd // adds gmail, twitter and spofify.
                    );

                Configurations conf2 =
                  DBHelper
                    .insertLeavingServices(
                      this,
                      configurationsDao,
                      servicesDao,
                      leavingDao,
                      confName,
                      servicesToAdd.subList(1,servicesToAdd.size()) // Adds twitter and spotify.
                    );

                if ((conf1 != null) && (conf2 != null) && (conf1.getId() == conf2.getId())) {
                    System.out.println(conf1.getArrivingServicesList().toString());
                    System.out.println(conf2.getLeavingServicesList().toString());
                    Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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


}
