//package fernandoperez.lifemanager.activities;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import fernandoperez.lifemanager.R;
//import fernandoperez.lifemanager.models.Configurations;
//import fernandoperez.lifemanager.models.ConfigurationsDao;
//import fernandoperez.lifemanager.models.Services;
//
//public class EditActivity extends AppCompatActivity {
//
//    ImageButton imB1;
//    ImageButton imB2;
//    ImageButton imB3;
//    ImageButton imB4;
//    ImageButton imB5;
//    ImageButton imB6;
//    String confname;
//
//
//private void cargaServ() {
//    List<Services> services = new ArrayList<>();
//    Configurations configuration = configurationsDao.queryBuilder()
//            .where(ConfigurationsDao.Properties.Name.eq(confname))
//            .unique();
//
//    services =configuration.getArrivingServices();
//
//    for(Iterator<Services> iterator = services.iterator(); iterator.hasNext();){
//        Services service = iterator.next();
//        if(service.getName() == "Twitter") tw=true;
//        if(service.getName() == "Spotify") sp=true;
//        if(service.getName() == "Gmail") gm=true;
//        if(service.getName() == "Bluetooth") bl=true;
//        if(service.getName() == "Wi-Fi") wi=true;
//        if(service.getName() == "GPS") gp=true;
//    }
//}
//
//    boolean tw = false;
//    boolean sp = false;
//    boolean gm = false;
//    boolean bl = false;
//    boolean wi = false;
//    boolean gp = false;
//
//
//    EditText configname;
//    String [] services;
//    String confName;
//
//    // WIFI parameters declaration
//    WifiManager wifiManager;
//    boolean on = false;
//
//    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    private final static int REQUEST_ENABLE_BT = 1;
//
//
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        confname = getIntent().getStringExtra("CONF_NAME");
//        setContentView(R.layout.activity_main);
//
//
//        //Buttons Declaration
//        imB1=(ImageButton) findViewById(R.id.imB1);
//        imB2=(ImageButton) findViewById(R.id.imB2);
//        imB3=(ImageButton) findViewById(R.id.imB3);
//        imB4=(ImageButton) findViewById(R.id.imB4);
//        imB5=(ImageButton) findViewById(R.id.imB5);
//        imB6=(ImageButton) findViewById(R.id.imB6);
//
//        wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
//        configname = (EditText) findViewById(R.id.edittext_activity_main);
//
//
//
//        imB1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB1.setSelected(!imB1.isSelected());
//                if (imB1.isSelected()) {
//                    //Handle selected state change
//                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitter));
//                    tw=true;
//                }
//                else {
//                    //Handle de-select state change
//                    imB1.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.twitterg));
//                    tw=false;
//                }
//
//
//            }
//
//        });
//
//        imB2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB2.setSelected(!imB2.isSelected());
//                if (imB2.isSelected()) {
//                    //Handle selected state change
//                    //imB1 = (ImageButton) setFeatureDrawable();
//
//                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spoti));
//                    sp=true;
//                }
//                else {
//                    //Handle de-select state change
//                    imB2.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.spotig));
//                    sp=false;
//                }
//
//
//            }
//
//        });
//
//        imB3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB3.setSelected(!imB3.isSelected());
//                if (imB3.isSelected()) {
//                    //Handle selected state change
//                    //imB1 = (ImageButton) setFeatureDrawable();
//
//                    imB3.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmail));
//                    gm=true;
//                }
//                else {
//                    //Handle de-select state change
//                    imB3.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gmailg));
//                    gm = false;
//                }
//
//
//            }
//
//        });
//
//        imB4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB4.setSelected(!imB4.isSelected());
//                if (imB4.isSelected()) {
//                    //Handle selected state change
//                    //imB1 = (ImageButton) setFeatureDrawable();
//
//                    imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.bluetooth));
//                    if (!mBluetoothAdapter.isEnabled()) {
//                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                        bl=true;
//                    }
//                }
//                else {
//                    //Handle de-select state change
//                    imB4.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.blg));
//                    mBluetoothAdapter.disable();
//                    bl=false;
//                }
//
//
//            }
//
//        });
//
//        imB5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB5.setSelected(!imB5.isSelected());
//                if (imB5.isSelected()) {
//                    //Handle selected state change
//                    //imB1 = (ImageButton) setFeatureDrawable();
//
//                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifi));
//                    if(wifiManager.isWifiEnabled()==false){
//                        wifiManager.setWifiEnabled(true);
//                        Toast.makeText(EditActivity.this, "wifi on",Toast.LENGTH_SHORT).show();
//                        wi=true;
//                    }
//                }
//                else {
//                    //Handle de-select state change
//                    imB5.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.wifig));
//                    if(wifiManager.isWifiEnabled()==true){
//                        wifiManager.setWifiEnabled(false);
//                        wi=false;
//                    }
//                }
//
//
//            }
//
//        });
//
//        imB6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imB6.setSelected(!imB6.isSelected());
//                if (imB6.isSelected()) {
//                    //Handle selected state change
//                    //imB1 = (ImageButton) setFeatureDrawable();
//
//                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gps));
//                    Intent gpsOptionsIntent = new Intent(
//                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(gpsOptionsIntent);
//                    gp=true;
//
//                }
//                else {
//                    //Handle de-select state change
//                    imB6.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.gpsg));
//                    gp=false;
//                }
//            }
//
//        });
//
//        confName=configname.toString();
//        /*
//        configuration.setName(confName);
//
//
//        List<String> servicesToAdd = new ArrayList<>();
//
//        if(tw = true) servicesToAdd.add("Twitter");
//        if(sp = true) servicesToAdd.add("Spotify");
//        if(gm = true) servicesToAdd.add("Gmail");
//        if(bl = true) servicesToAdd.add("Bluetooth");
//        if(wi = true) servicesToAdd.add("Wi-Fi");
//        if(gp = true) servicesToAdd.add("GPS");
//
//        Configurations config =DBHelper.insertArrivingServices(
//                this,
//                configurationsDao,
//                servicesDao,
//                arrivigDao,
//                confName,//EDIT TEXT FINAL IT'S A STRIGN
//                servicesToAdd
//        );
//        */
//
//
//
//
//    }
//
//
//}
//
//
