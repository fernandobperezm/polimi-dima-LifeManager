package fernandoperez.lifemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.utils.constants;

public class ConfigurationListActivity extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private ConfigurationsDao configurationsDao;

    private int selectedConfigurationPosition = -1;

    private void loadData(){
        data.clear();
        List<Configurations> configurationsList = configurationsDao.loadAll();
        for (Configurations configurations : configurationsList) {
            data.add(configurations.getName());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ListView lv = (ListView) findViewById(R.id.listview);
        loadData();
        adapter= new MyListAdapter(this, R.layout.row_configurationlist, data);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_configurationlist);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_configuration_list);
        }

        ListView lv = (ListView) findViewById(R.id.listview);

        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();

        loadData();

        adapter = new MyListAdapter(this, R.layout.row_configurationlist,data);
        lv.setAdapter(adapter);

        if (savedInstanceState != null) {
            selectedConfigurationPosition = savedInstanceState.getInt("CONF_POSITION", -1);
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CONF_POSITION", selectedConfigurationPosition);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configlist, menu);
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
            case R.id.action_addconfig:
                intent = new Intent (getApplicationContext(), AddConfigurationActivity.class);
                intent.putExtra(constants.CONFIGURATION_NAME, "");
                startActivity(intent);
                return true;

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


    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<Switch> mSwitchList;

        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            layout = resource;
            mSwitchList = new ArrayList<>(objects.size());
        }

        @Override
        @NonNull
        public View getView (final int position, View convertView, @NonNull ViewGroup parent){
            ViewHolder mainViewholder;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.nombre = (TextView) convertView.findViewById(R.id.list_text);
                viewHolder.nombre.setText(getItem(position));

                viewHolder.edit = (Button) convertView.findViewById(R.id.button_edit);
                viewHolder.delete = (Button) convertView.findViewById(R.id.button_delete);
                viewHolder.sw = (Switch) convertView.findViewById(R.id.sw);

                mSwitchList.add(viewHolder.sw);

                if (selectedConfigurationPosition == position) {
                    turnOffAllSwitches();
                    viewHolder.sw.setChecked(true);
                }

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (getApplicationContext(), AddConfigurationActivity.class);
                        intent.putExtra(constants.CONFIGURATION_NAME,viewHolder.nombre.getText().toString());
                        startActivity(intent);
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnOffAllSwitches();
                        selectedConfigurationPosition = -1;
                        adapter.remove(viewHolder.nombre.getText().toString());
                        configurationsDao.delete(
                                configurationsDao.queryBuilder()
                                .where(ConfigurationsDao.Properties.Name.eq(viewHolder.nombre.getText().toString()))
                                .unique()
                        );
                    }
                });

                viewHolder.sw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedConfigurationPosition = position;
                        Intent intent = new Intent (getApplicationContext(), ScreenSlideActivity.class);
                        intent.putExtra(constants.CONFIGURATION_NAME, viewHolder.nombre.getText().toString());
                        startActivity(intent);
                    }
                });

                convertView.setTag(viewHolder);
            }

            else{
                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.nombre.setText(getItem(position));
                mSwitchList.add(mainViewholder.sw);

                if (selectedConfigurationPosition == position) {
                    turnOffAllSwitches();
                    mainViewholder.sw.setChecked(true);
                }
            }

            return convertView;
        }

        private void turnOffAllSwitches() {
            for (Switch sw : mSwitchList) {
                sw.setChecked(false);
            }
        }
    }

    public class ViewHolder{
        TextView nombre;
        Button edit;
        Button delete;
        Switch sw;

    }
}
