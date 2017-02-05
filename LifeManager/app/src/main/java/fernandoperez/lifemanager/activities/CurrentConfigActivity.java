package fernandoperez.lifemanager.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.adapters.ServicesRecyclerAdapter;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.utils.RecyclerItemClickListener;

public class CurrentConfigActivity extends AppCompatActivity {

    // The member adapter for the current config.
    ServicesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_config);
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

        // We set here the services view.
        //TODO: receive from an intent the current config and with that the services to show.
        setUpServicesCard();
    }

    /**
     * The method setUpServicesCard loads all the services into a services list and gives the control
     * to the method loadRecyclerView. It has a progress dialog that tells the user how much is left
     * until the services are completely loaded.
     */
    private void setUpServicesCard() {
        final ProgressDialog progressDialog =
                ProgressDialog.show(this, "Wait", "Your services are loading...", true, false);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Some testing services
//        List<Services> servicesList = new ArrayList<Services>();
//        servicesList.add(new Services("Spotify","Spotify Inc."));
//        servicesList.add(new Services("Twitter", "Twitter Inc."));
//        servicesList.add(new Services("Wifi","Spotify Inc."));
//        servicesList.add(new Services("GPS", "Twitter Inc."));
//        servicesList.add(new Services("Weather","Spotify Inc."));
//        servicesList.add(new Services("Facebook", "Twitter Inc."));

//        loadRecyclerView(servicesList);
        progressDialog.dismiss();
    }

    /**
     * The loadRecyclerView method receives a list of services and load them into the recycler view,
     * so they can be shown in cards, also, it sets an item click listener so when a feed is touched
     * it opens the correspondent feed.
     * @param services The list of services to display.
     */
    public void loadRecyclerView(List<Services> services){

        // Find the recycler view, this view will help us to show the feeds as cards.
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.services_list);
        recyclerView.setHasFixedSize(true);

        // TODO: Add click listeners to the adapter so the feeds can be accessed.
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Services service = mAdapter.get(position);

            }
        }));

        // Set the elements of the adapter, this is, the services the user has for this config.
        mAdapter = new ServicesRecyclerAdapter(services);

        // Set the card view of each in the recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }

}
