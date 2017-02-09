package fernandoperez.lifemanager.utils;

/**
 * Created by fernandoperez on 1/26/17.
 */

public abstract class constants {
    // INTENT CODES.
    public final static int TWITTER_MAX_HOME_TIMELINE_TWEETS = 50;

    // Call codes.
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER = 1000;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_OK = 1001;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_ALREADY_CREATED = 1002;

    // Names.
    public final static String GOOGLE_DRIVE_FOLDER_NAME = "GOOGLE_DRIVE_FOLDER_NAME";

    // DEFAULT VALUES.
    public final static int SPOTIFY_DEFAULT_INDEX_PLAYLIST = 0;

    // Enumarable.
    public enum SERVICES_LIST {
        EMAIL, SPOTIFY, TWITTER, LOCATION, WIFI, BLUETOOTH
    }

    //
    public final static int MAX_SERVICES = 6;

    // Gmail API.
    public final static long MAX_EMAILS_FETCH = 20;
}

