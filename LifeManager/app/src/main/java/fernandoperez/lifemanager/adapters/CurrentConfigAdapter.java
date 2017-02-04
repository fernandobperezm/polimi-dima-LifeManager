package fernandoperez.lifemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Services;

/**
 * The CurrentConfigAdapter class is the implementation of an ArrayAdapter of Services, which
 * will hold all the services of the currently selected service, this array adapter will provide
 * the cardview of every service in the Current Config Activity and it's content will be filled there.
 */
public class CurrentConfigAdapter extends ArrayAdapter<Services> {

    // Class Constructor.
    public CurrentConfigAdapter(Context context, List<Services> servicesList){
        super(context, 0, servicesList);
    }

    // TODO: discover what's this.
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Services services = getItem(position);
        CurrentServicesViewHolder currentServicesViewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.service_row, null);

            currentServicesViewHolder = new CurrentServicesViewHolder(convertView);
            convertView.setTag(currentServicesViewHolder);
        }
        else {
            currentServicesViewHolder = (CurrentServicesViewHolder) convertView.getTag();
        }

        currentServicesViewHolder.nameTextView.setText(services.getName());
        currentServicesViewHolder.companyTextView.setText(services.getCompany());

        return convertView;
    }

    /**
     * The CurrentServicesViewHolder class is a static class that receives a card view and identifies
     * all the items like TextVies, EditText and others inside the Services Row.
     */
    static class CurrentServicesViewHolder {

        TextView nameTextView;
        TextView companyTextView;

        public CurrentServicesViewHolder(View view){
            nameTextView = (TextView) view.
                    findViewById(R.id.service_setting_name);
            companyTextView = (TextView) view.
                    findViewById(R.id.service_setting_description);
        }

    }

}

