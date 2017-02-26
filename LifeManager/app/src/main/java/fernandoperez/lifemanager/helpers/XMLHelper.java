package fernandoperez.lifemanager.helpers;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.models.ArrivingConfWithServDao;
import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.models.ConfigurationsDao;
import fernandoperez.lifemanager.models.DaoSession;
import fernandoperez.lifemanager.models.LeavingConfWithServDao;
import fernandoperez.lifemanager.models.ServicesDao;
import fernandoperez.lifemanager.parsers.BackupParser;
import fernandoperez.lifemanager.utils.Triplet;
import fernandoperez.lifemanager.utils.constants;

/**
 * Created by fernandoperez on 2/15/17.
 */

public class XMLHelper {

    public static void makeLocalBackup(Context context, ConfigurationsDao configurationsDao) {
        StringWriter writer = new StringWriter();
        XmlSerializer xmlSerializer = Xml.newSerializer();

        try {
            FileOutputStream fileos = context.openFileOutput(constants.BACKUP_FILENAME, Context.MODE_PRIVATE);
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "Backup");

            List<Configurations> backupConfigurations = DBHelper.getAllConfigurations(configurationsDao);
            for (Iterator<Configurations> iterator = backupConfigurations.iterator(); iterator.hasNext(); ){
                Configurations conf = iterator.next();
                conf.toXML(xmlSerializer);
            }

            xmlSerializer.endTag(null, "Backup");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            Toast.makeText(context, "Backup made sucessfully.", Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException | IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readLocalBackup(Context context, DaoSession daoSession) {
        ConfigurationsDao configurationsDao = daoSession.getConfigurationsDao();
        ServicesDao servicesDao = daoSession.getServicesDao();
        ArrivingConfWithServDao arrivingDao = daoSession.getArrivingConfWithServDao();
        LeavingConfWithServDao leavingDao = daoSession.getLeavingConfWithServDao();

        FileInputStream inputStream;
        InputStreamReader inputStreamReader;
        BackupParser backupParser = new BackupParser();

        try {
            inputStream = context.openFileInput(constants.BACKUP_FILENAME);
            inputStreamReader = new InputStreamReader(inputStream);
            List<Triplet<Configurations, List<String>, List<String>>> configurationWithServices = backupParser.parse(inputStreamReader);
            inputStream.close();

            if (configurationWithServices.size() > 0) {
                configurationsDao.deleteAll();
                daoSession.getArrivingConfWithServDao().deleteAll();
                daoSession.getLeavingConfWithServDao().deleteAll();
            }

            for (Iterator<Triplet<Configurations, List<String>, List<String>>> iterator = configurationWithServices.iterator(); iterator.hasNext();  ){
                Triplet<Configurations, List<String>, List<String>> triplet = iterator.next();
                Configurations configurations = triplet.first;
                List<String> arrivingServices = triplet.second;
//                List<String> leavingServices = triplet.third;

                DBHelper.insertConfiguration(context,configurationsDao, configurations);
                DBHelper.insertArrivingServices(context, configurationsDao, servicesDao, arrivingDao, configurations.getName(), arrivingServices);
//                DBHelper.insertLeavingServices(context, configurationsDao, servicesDao, leavingDao, configurations.getName(), leavingServices);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "No previous backup exist.",Toast.LENGTH_SHORT).show();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }


    }
}
