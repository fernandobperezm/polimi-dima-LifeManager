package fernandoperez.lifemanager.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.ArrivingConfWithServDao;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.LeavingConfWithServDao;
import fernandoperez.lifemanager.models.ServicesDao;

public class MainActivity extends AppCompatActivity {

    // Services.
    ImageButton imB1; boolean tw = false;
    ImageButton imB2; boolean sp = false;
    ImageButton imB3; boolean gm = false;
    ImageButton imB4; boolean bl = false;
    ImageButton imB5; boolean wi = false;
    ImageButton imB6; boolean gp = false;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

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

        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();
        servicesDao = daoSession.getServicesDao();
        arrivingDao = daoSession.getArrivingConfWithServDao();
        leavingDao = daoSession.getLeavingConfWithServDao();

        // Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Buttons Declaration
        imB1=(ImageButton) findViewById(R.id.imB1);
        imB2=(ImageButton) findViewById(R.id.imB2);
        imB3=(ImageButton) findViewById(R.id.imB3);
        imB4=(ImageButton) findViewById(R.id.imB4);
        imB5=(ImageButton) findViewById(R.id.imB5);
        imB6=(ImageButton) findViewById(R.id.imB6);

        wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
        configname = (EditText) findViewById(R.id.edittext_activity_main);

        imB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB1.setSelected(!imB1.isSelected());
                if (imB1.isSelected()) {
                    //Handle selected state change
                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitter));
                    tw=true;
                }
                else {
                    //Handle de-select state change
                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitterg));
                    tw=false;
                }
            }
        });

        imB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB2.setSelected(!imB2.isSelected());
                if (imB2.isSelected()) {

                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spoti));
                    sp=true;
                }
                else {
                    //Handle de-select state change
                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spotig));
                    sp=false;
                }


            }

        });

        imB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB3.setSelected(!imB3.isSelected());
                if (imB3.isSelected()) {
                    //Handle selected state change
                    //imB1 = (ImageButton) setFeatureDrawable();

                    imB3.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmail));
                    gm=true;
                }
                else {
                    //Handle de-select state change
                    imB3.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmailg));
                    gm = false;
                }


            }

        });

        imB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB4.setSelected(!imB4.isSelected());
                if (imB4.isSelected()) {
                    //Handle selected state change
                    //imB1 = (ImageButton) setFeatureDrawable();

                    imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.bluetooth));

                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        bl=true;
                    }
                }
                else {
                    //Handle de-select state change
                    imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.blg));
                    mBluetoothAdapter.disable();
                    bl=false;
                }


            }

        });

        imB5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB5.setSelected(!imB5.isSelected());
                if (imB5.isSelected()) {

                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifi));
                    if(wifiManager.isWifiEnabled()==false){
                        wifiManager.setWifiEnabled(true);
                        Toast.makeText(MainActivity.this, "wifi on",Toast.LENGTH_SHORT).show();
                        wi=true;
                    }
                }
                else {
                    //Handle de-select state change
                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifig));
                    if(wifiManager.isWifiEnabled()==true){
                        wifiManager.setWifiEnabled(false);
                        wi=false;
                    }
                }


            }

        });

        imB6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB6.setSelected(!imB6.isSelected());
                if (imB6.isSelected()) {

                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gps));
                    Intent gpsOptionsIntent = new Intent(
                      android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                    gp=true;

                }
                else {
                    //Handle de-select state change
                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gpsg));
                    gp=false;
                }
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


}
