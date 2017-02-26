package fernandoperez.lifemanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;

public class ConfigurationListActivity extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter ;

    private DaoSession daoSession;
    private ConfigurationsDao configurationsDao;

    private void cargaDatos(){
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
//        setContentView(R.layout.activity_configuration_list);
        cargaDatos();
        adapter= new MyListAdapter(this, R.layout.row_configurationlist,data);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_configurationlist);
        setSupportActionBar(toolbar);

        FloatingActionButton adds = (FloatingActionButton) findViewById(R.id.fab_activity_configurationlist);
        ListView lv = (ListView) findViewById(R.id.listview);

        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), AddConfigurationActivity.class);
                intent.putExtra("CONF_NAME", "");
                startActivity(intent);
            }
        });

        daoSession = ((MyApplication) getApplication()).getDaoSession();
        configurationsDao = daoSession.getConfigurationsDao();

        cargaDatos();

        adapter = new MyListAdapter(this, R.layout.row_configurationlist,data);
        lv.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            layout = resource;
        }
        @Override
        @NonNull
        public View getView (final int position, View convertView, @NonNull ViewGroup parent){
            ViewHolder mainViewholder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.nombre = (TextView) convertView.findViewById(R.id.list_text);
                viewHolder.nombre.setText(getItem(position));

                viewHolder.edit = (Button) convertView.findViewById(R.id.button_edit);
                viewHolder.delete = (Button) convertView.findViewById(R.id.button_delete);
                viewHolder.sw = (Switch) convertView.findViewById(R.id.sw);

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"EDIT PRESSED at position "+ position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (getApplicationContext(), AddConfigurationActivity.class);
                        intent.putExtra("CONF_NAME",viewHolder.nombre.getText().toString());
                        startActivity(intent);
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"DELETE PRESSED at position "+ position, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(),"SW PRESSED", Toast.LENGTH_LONG).show();
                    }
                });

                convertView.setTag(viewHolder);
            }

            else{
                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.nombre.setText(getItem(position));
            }
            return convertView;
        }
    }

    public class ViewHolder{

        TextView nombre;
        Button edit;
        Button delete;
        Switch sw;

    }
}
