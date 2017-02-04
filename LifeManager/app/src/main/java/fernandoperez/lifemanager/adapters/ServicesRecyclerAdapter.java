package fernandoperez.lifemanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Services;

/**
 * Created by fernandoperez on 2/4/17.
 */

/**
 * The class ServicesRecyclerAdapter inherits from Adapter<ViewHolder> in which the recycler
 * adapter, this is a container for displaying large data sets that can be scrolled very efficiently
 * by maintaining a limited number of views.
 */
public class ServicesRecyclerAdapter extends RecyclerView.Adapter<ServicesRecyclerAdapter.ServiceViewHolder> {

    private List<Services> mServicesList;
    private List<Services> mAllServices;
    private Context context;

    /**
     * The constructor of the class.
     * @param servicesToShow the list of services we want to show at this moment.
     */
    public ServicesRecyclerAdapter(List<Services> servicesToShow) {
        this.mServicesList = new ArrayList<>(servicesToShow);
    }

    /**
     * Creates the View Holder for our services, the View to load will be a card loaded from the
     * layout service_card.xml
     * @param parent the parent ViewGroup, defined to override.
     * @param viewType the type of viewType, defined to override.
     * @return a new ServiceViewHolder instance.
     */
    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null){
            context = parent.getContext();
        }

        View card = LayoutInflater.from(context).inflate(R.layout.service_card, parent, false);

        return new ServiceViewHolder(card);
    }

    /**
     * The method onBindViewHolder set the contents of every element of the ServiceViewHolder at
     * binding moment.
     * @param holder a ServiceViewHolder instance.
     * @param position the position of the element in the View Holder to bind.
     */
    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Services services = get(position);

        holder.vName.setText(services.getName());
        holder.vCompany.setText(services.getCompany());
    }

    /**
     * The getItemCount function is called to retrieve the number of elements of the services list.
     * @return the number of element of the services list.
     */
    @Override
    public int getItemCount() {
        return mServicesList.size();
    }

    /**
     * The function get returns the Service at position indicated by position.
     * @param position the index position to retrieve the service.
     * @return the service inside the services list at position position.
     */
    public Services get(int position){
        return mServicesList.get(position);
    }

    /**
     * The method allServices is called when we have all the services in a single list and we want
     * to add them in a single call.
     * @param allServices the list of services to be added into the Recycler View.
     */
    public void setAllServices(List<Services> allServices){
        this.mAllServices = new ArrayList<>(allServices);
        this.mServicesList = new ArrayList<>(allServices);
        this.notifyDataSetChanged();
    }


    /**
     * The class ServiceViewHolder it's an static class that provides a View Holder to the recycler view.
     * In our case, it provides the Card View so we can display every Service in the feed as a card
     * element.
     */
    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vCompany;


        public ServiceViewHolder(View card) {
            super(card);

            vName = (TextView) card.findViewById(R.id.serviceName);
            vCompany = (TextView) card.findViewById(R.id.serviceCompany);
        }
    }
}
