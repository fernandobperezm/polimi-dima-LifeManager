package fernandoperez.lifemanager.helpers;

import android.content.Context;
import android.database.SQLException;
import android.widget.Toast;

import java.util.List;

import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.models.ServicesDao;
import fernandoperez.lifemanager.utils.constants;

/**
 * Created by fernandoperez on 2/15/17.
 */

public class DBHelper {

    public static void saveList(Configurations configuration, List<Services> servicesList, constants.CONFIGURATION_TYPES configurationType) {
    }

    public static void insertConfiguration(Context context, ConfigurationsDao configurationsDao, Configurations configuration) {
        try {
            configurationsDao.insert(configuration);
            Toast.makeText(context, "Successfully added a new configuration.", Toast.LENGTH_SHORT).show();
        } catch (SQLException exception) {
            Toast.makeText(context, "Configuration already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public static void insertServices(ServicesDao servicesDao) {

        Services spotify = new Services(null, "Spotify", constants.SERVICES_LIST.SPOTIFY );
        Services twitter = new Services(null, "Twitter", constants.SERVICES_LIST.TWITTER );
        Services gmail = new Services(null, "Gmail", constants.SERVICES_LIST.EMAIL );

        try {
            servicesDao.insert(spotify);
            servicesDao.insert(twitter);
            servicesDao.insert(gmail);
        } catch (SQLException exception) {
        }
    }
}
