package fernandoperez.lifemanager.helpers;

import android.content.Context;
import android.database.SQLException;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.models.ArrivingConfWithServ;
import fernandoperez.lifemanager.models.ArrivingConfWithServDao;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.LeavingConfWithServ;
import fernandoperez.lifemanager.models.LeavingConfWithServDao;
import fernandoperez.lifemanager.models.Services;
import fernandoperez.lifemanager.models.ServicesDao;
import fernandoperez.lifemanager.utils.constants;

/**
 * Created by fernandoperez on 2/15/17.
 */

public class DBHelper {
    public static final	String TABLE_NAME =	"entry";
    public static final	String COLUMN_NAME_ENTRY_ID	= "entryid";
    public static final	String COLUMN_NAME_TITLE	=	"title";
    public static final	String COLUMN_NAME_SUBTITLE	=	"subtitle";
    private	static final String TEXT_TYPE	=	"	TEXT";
    private	static final String	COMMA_SEP	=	",";
    private	static final String	SQL_CREATE_ENTRIES	=

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
        //TODO: Better handle of this error.
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

    public static Configurations insertArrivingServices(
            Context context,
            ConfigurationsDao configurationsDao,
            ServicesDao servicesDao,
            ArrivingConfWithServDao arrivingDao,
            String confName,
            String[] servicesToAdd) {

        Configurations configurationForServices = configurationsDao.queryBuilder()
          .where(ConfigurationsDao.Properties.Name.eq(confName))
          .orderAsc(ConfigurationsDao.Properties.Name)
          .unique();

        if (configurationForServices == null) {
            Toast.makeText(context, "No Configuration found",Toast.LENGTH_SHORT).show();
            return null;
        }

        // TODO: Handle better this, we must be able to only search on the ones given by the user.
        List<Services> arrivingList =
          servicesDao.queryBuilder()
            .whereOr(
              ServicesDao.Properties.Name.eq("Spotify"),
              ServicesDao.Properties.Name.eq("Twitter"),
              ServicesDao.Properties.Name.eq("Email"))
            .orderAsc(
              ServicesDao.Properties.ServiceType)
            .list();

        if (arrivingList == null) {
            Toast.makeText(context, "No services to add at arriving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            for (Iterator<Services> arrivingIterator = arrivingList.iterator(); arrivingIterator.hasNext();) {
                arrivingDao.insert(
                  new ArrivingConfWithServ(
                    null,
                    configurationForServices.getId(),
                    arrivingIterator.next().getId()
                  )
                );
            }
        } catch (SQLException exception) {
            return null;
        }

        return configurationForServices;
    }

    public static Configurations insertLeavingServices(
            Context context,
            ConfigurationsDao configurationsDao,
            ServicesDao servicesDao,
            LeavingConfWithServDao leavingDao,
            String confName,
            String[] servicesToAdd) {

        Configurations configurationForServices = configurationsDao.queryBuilder()
          .where(ConfigurationsDao.Properties.Name.eq(confName))
          .orderAsc(ConfigurationsDao.Properties.Name)
          .unique();

        if (configurationForServices == null) {
            Toast.makeText(context, "No Configuration found",Toast.LENGTH_SHORT).show();
            return null;
        }

        // TODO: Handle better this, we must be able to only search on the ones given by the user.
        List<Services> leavingList =
          servicesDao.queryBuilder()
            .whereOr(
              ServicesDao.Properties.Name.eq("Spotify"),
              ServicesDao.Properties.Name.eq("Twitter"),
              ServicesDao.Properties.Name.eq("Email"))
            .orderAsc(
              ServicesDao.Properties.ServiceType)
            .list();

        if (leavingList == null || leavingList.size() == 0) {
            Toast.makeText(context, "No services to add at leaving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            for (Iterator<Services> leavingIterator = leavingList.iterator(); leavingIterator.hasNext();) {
                leavingDao.insert(
                  new LeavingConfWithServ(
                    null,
                    configurationForServices.getId(),
                    leavingIterator.next().getId()
                  )
                );
            }
        } catch (SQLException exception) {
            return null;
        }

        return configurationForServices;
    }

    private static String generateWhereOrString(String[] stringArrays) {
        return null
    }

}
