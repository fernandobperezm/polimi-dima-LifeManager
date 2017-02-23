package fernandoperez.lifemanager.helpers;

import android.content.Context;
import android.database.SQLException;
import android.widget.Toast;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static void saveList(Configurations configuration, List<Services> servicesList, constants.CONFIGURATION_TYPES configurationType) {
    }

    public static List<Configurations> getAllConfigurations(ConfigurationsDao configurationsDao) {
        return configurationsDao.loadAll();
    }

    /**
     * The method insertConfiguration receives an instance of a Configuration and an instance of
     * ConfigurationsDao and tries to insert the Configuration into the Configurations table.
     * @param context the context that called the method.
     * @param configurationsDao an instance of ConfigurationsDao.
     * @param configuration an instance of Configuration.
     */
    public static void insertConfiguration(Context context, ConfigurationsDao configurationsDao, Configurations configuration) {
        try {
            configurationsDao.insert(configuration);
            Toast.makeText(context, "Successfully added a new configuration.", Toast.LENGTH_SHORT).show();
        } catch (SQLException exception) {
            Toast.makeText(context, "Configuration already exists", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The method insertServices receives an instance of ServiceDao and tries to insert the services
     * provided by the app into the Services table.
     * @param servicesDao an instance of ServicesDao.
     */
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

    /**
     * The inserLeavingServices function receives a configuration name and a list of service's name
     * and tries to insert them into the ArrivingConfWithServ table if the services anc configuration
     * exists.
     * @param context the context that called the method.
     * @param configurationsDao the DAO's object for the configuration, it let us retrieve the configuration the user wants to add services.
     * @param servicesDao the DAO's object for the services, it let us find services in the database.
     * @param arrivingDao the DAO's object for the arriving services for a specific configuration.
     * @param confName the configuration name the user wants to add services.
     * @param servicesToAdd a String list that has the names of the services the user wants to add.
     * @return An instance of a Configurations object retrieved from the Database if the insertion succeeded, null otherwise.
     */
    public static Configurations insertArrivingServices(
            Context context,
            ConfigurationsDao configurationsDao,
            ServicesDao servicesDao,
            ArrivingConfWithServDao arrivingDao,
            String confName,
            List<String> servicesToAdd) {

        // TODO: Figure out how to not insert the same elements (now inserting repetitions).
        Configurations configurationForServices = configurationsDao.queryBuilder()
          .where(ConfigurationsDao.Properties.Name.eq(confName))
          .orderAsc(ConfigurationsDao.Properties.Name)
          .unique();

        if (configurationForServices == null) {
            Toast.makeText(context, "No Configuration found",Toast.LENGTH_SHORT).show();
            return null;
        }

        if (servicesToAdd.size() == 0) {
            Toast.makeText(context, "No services to add at arriving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        QueryBuilder<Services> queryBuilder = servicesDao.queryBuilder();
        List<Services> arrivingList =
          servicesDao.queryBuilder()
            .where(generateConditions(servicesToAdd, queryBuilder))
            .orderAsc(
              ServicesDao.Properties.ServiceType)
            .list();

        if (arrivingList == null) {
            Toast.makeText(context, "No services to add at arriving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        // Search for the list of entities with the current config.
        List<ArrivingConfWithServ> arrivingConfWithServList =
          arrivingDao.queryBuilder()
            .where(ArrivingConfWithServDao.Properties.ConfigurationId.eq(configurationForServices.getId()))
            .list();

        try {
            arrivingDao.deleteInTx(arrivingConfWithServList);
        } catch (SQLException exception) {
            exception.printStackTrace();
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

    /**
     * The inserLeavingServices function receives a configuration name and a list of service's name
     * and tries to insert them into the LeavingConfWithServ table if the services anc configuration
     * exists.
     * @param context the context that called the method.
     * @param configurationsDao the DAO's object for the configuration, it let us retrieve the configuration the user wants to add services.
     * @param servicesDao the DAO's object for the services, it let us find services in the database.
     * @param leavingDao the DAO's object for the leaving services for a specific configuration.
     * @param confName the configuration name the user wants to add services.
     * @param servicesToAdd a String list that has the names of the services the user wants to add.
     * @return An instance of a Configurations object retrieved from the Database if the insertion succeeded, null otherwise.
     */
    public static Configurations insertLeavingServices(
            Context context,
            ConfigurationsDao configurationsDao,
            ServicesDao servicesDao,
            LeavingConfWithServDao leavingDao,
            String confName,
            List<String> servicesToAdd) {

        Configurations configurationForServices = configurationsDao.queryBuilder()
          .where(ConfigurationsDao.Properties.Name.eq(confName))
          .orderAsc(ConfigurationsDao.Properties.Name)
          .unique();

        if (configurationForServices == null) {
            Toast.makeText(context, "No Configuration found",Toast.LENGTH_SHORT).show();
            return null;
        }

        if (servicesToAdd.size() == 0) {
            Toast.makeText(context, "No services to add at leaving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        QueryBuilder<Services> queryBuilder = servicesDao.queryBuilder();
        List<Services> leavingList =
          servicesDao.queryBuilder()
            .where(generateConditions(servicesToAdd, queryBuilder))
            .orderAsc(
              ServicesDao.Properties.ServiceType)
            .list();

        if (leavingList == null || leavingList.size() == 0) {
            Toast.makeText(context, "No services to add at leaving.",Toast.LENGTH_SHORT).show();
            return null;
        }

        // Search for the list of entities with the current config.
        List<LeavingConfWithServ> leavingConfWithServList =
          leavingDao.queryBuilder()
            .where(ArrivingConfWithServDao.Properties.ConfigurationId.eq(configurationForServices.getId()))
            .list();

        try {
            leavingDao.deleteInTx(leavingConfWithServList);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

        try {
            for (Iterator<Services> leavingIterator = leavingList.iterator(); leavingIterator.hasNext();) {
                leavingDao.save(
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

    /**
     * The function generateConditions returns a WhereCondition based on the services the user wants
     * to add
     * @param stringArrays a String List representing the services names.
     * @param queryBuilder a Service Query Builder that let us perform the OR operator.
     * @return a WhereCondition representing the OR of all the services that the user want to add.
     */
    private static WhereCondition generateConditions(List<String> stringArrays, QueryBuilder<Services> queryBuilder) {
        /**
         * Precondition: stringArrays.size > 0.
         */

        List<WhereCondition> conditionList = new ArrayList<>();

        for (Iterator<String> iterator = stringArrays.iterator(); iterator.hasNext(); ) {
            conditionList.add(ServicesDao.Properties.Name.eq(iterator.next()));
        }

        WhereCondition[] conditionsArray = new WhereCondition[conditionList.size()];
        conditionsArray = conditionList.toArray(conditionsArray);

        if (conditionList.size() == 1) {
            return conditionsArray[0];
        } else {
            return queryBuilder.or(
                    conditionsArray[0],
                    conditionsArray[1],
                    Arrays.copyOfRange(conditionsArray, 2,conditionsArray.length));
        }
    }

}
