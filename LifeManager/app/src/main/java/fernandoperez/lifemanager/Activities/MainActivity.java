package fernandoperez.lifemanager.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.SQLException;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.utils.constants;

public class MainActivity extends AppCompatActivity {

    ImageButton imB1;
    ImageButton imB2;
    ImageButton imB3;
    ImageButton imB4;
    ImageButton imB5;
    ImageButton imB6;
    RelativeLayout bg;
    // WIFI parameters declaration
    WifiManager wifiManager;
    boolean on = false;

    BluetoothAdapter mBluetoothAdapter;

    private DaoSession daoSession;
    private ConfigurationsDao configurationsDao;

    private final static int REQUEST_ENABLE_BT = 1;

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

        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();

        //Test for background
        bg=(RelativeLayout) findViewById(R.id.content_main);
        //Buttons Declaration
        imB1=(ImageButton) findViewById(R.id.imB1);
        imB2=(ImageButton) findViewById(R.id.imB2);
        imB3=(ImageButton) findViewById(R.id.imB3);
        imB4=(ImageButton) findViewById(R.id.imB4);
        imB5=(ImageButton) findViewById(R.id.imB5);
        imB6=(ImageButton) findViewById(R.id.imB6);
        wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        imB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB1.setSelected(!imB1.isSelected());
                if (imB1.isSelected()) {
                    //Handle selected state change

                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitter));
                }
                else {
                    //Handle de-select state change
                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitterg));
                }


            }

        });

        imB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB2.setSelected(!imB2.isSelected());
                if (imB2.isSelected()) {
                    //Handle selected state change
                    //imB1 = (ImageButton) setFeatureDrawable();

                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spoti));
                }
                else {
                    //Handle de-select state change
                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spotig));
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
                }
                else {
                    //Handle de-select state change
                    imB3.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmailg));
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
                    if(mBluetoothAdapter == null) {
                        System.out.println("NOT BLUETOOTH.");
                    } else {
                        imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.bluetooth));
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        }
                    }
                }
                else {
                    //Handle de-select state change
                    imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.blg));
                    mBluetoothAdapter.disable();
                }


            }

        });

        imB5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB5.setSelected(!imB5.isSelected());
                if (imB5.isSelected()) {
                    //Handle selected state change
                    //imB1 = (ImageButton) setFeatureDrawable();

                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifi));
                    if(wifiManager.isWifiEnabled()==false){
                        wifiManager.setWifiEnabled(true);
                        Toast.makeText(MainActivity.this, "wifi on",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //Handle de-select state change
                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifig));
                    if(wifiManager.isWifiEnabled()==true){
                        wifiManager.setWifiEnabled(false);
                    }
                }


            }

        });

        imB6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imB6.setSelected(!imB6.isSelected());
                if (imB6.isSelected()) {
                    //Handle selected state change
                    //imB1 = (ImageButton) setFeatureDrawable();

                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gps));
                    Intent gpsOptionsIntent = new Intent(
                      android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);

                }
                else {
                    //Handle de-select state change
                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gpsg));
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

                String confName = "Home";

                Configurations configuration = new Configurations(null, confName);

                DBHelper.insertConfiguration(this, configurationsDao, configuration);

//                List<Services> servicesLeaving =
//
////
////                Configurations configuration = new Configurations(confName);
////                if (configuration.save() >= 1) {
////                } else {
////                    System.out.println("DIDNT SAVE");
////                }
////
////                DBHelper.saveList(configuration, servicesArriving, constants.CONFIGURATION_TYPES.ARRIVING);
////                DBHelper.saveList(configuration, servicesLeaving, constants.CONFIGURATION_TYPES.LEAVING);
////
////                for (Iterator<ArrivingConfWithServ> iterator = ArrivingConfWithServ.findAll(ArrivingConfWithServ.class); iterator.hasNext();) {
////                    System.out.println(iterator.next().toString());
//                }
//
//                intent.putExtra(constants.CONFIGURATION_NAME, confName);
//                intent.putExtra(constants.CONFIGURATION_CURRENT_TYPE, constants.CONFIGURATION_TYPES.ARRIVING);

//                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
