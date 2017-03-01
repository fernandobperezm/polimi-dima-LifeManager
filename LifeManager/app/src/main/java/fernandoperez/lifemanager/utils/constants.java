package fernandoperez.lifemanager.utils;

import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;

/**
 * Created by fernandoperez on 1/26/17.
 */

public abstract class constants {
    // INTENT CODES.
    public final static int TWITTER_MAX_HOME_TIMELINE_TWEETS = 50;
    public final static int REQUEST_ENABLE_BT = 69;

    public final static String CONFIGURATION_NAME = "CONFIGURATION_NAME";
    public final static String CONFIGURATION_CURRENT_TYPE = "CONFIGURATION_TYPE";

    //
    public final static String BACKUP_FILENAME = "backup.xml";

    // Call codes.
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER = 1000;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_OK = 1001;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_ALREADY_CREATED = 1002;


    // Enumerable.
    public enum SERVICES_LIST {
        TWITTER(0), SPOTIFY(1), EMAIL(2), WIFI(3), LOCATION(4), BLUETOOTH(5);

        public final int id;

        SERVICES_LIST(int id) {
            this.id = id;
        }
    }

    public enum CONFIGURATION_TYPES {
        ARRIVING(0), LEAVING(1);

        public final int id;

        CONFIGURATION_TYPES(int id) {
            this.id = id;
        }
    }

    // Gmail API.
    public final static long MAX_EMAILS_FETCH = 20;

}

