package fernandoperez.lifemanager.utils;

/**
 * Created by fernandoperez on 1/26/17.
 */

public abstract class constants {


    // INTENT CODES.
    public final static String TWITTER_ID_INTENT = "TWITTER_ID_INTENT";
    public final static String SPOTIFIY_INDEX_INTENT = "SPOTIFIY_INDEX_INTENT";
    public final static long DEFAULT_TWITTER_USER_ID = -1;
    public final static int TWITTER_MAX_HOME_TIMELINE_TWEETS = 50;



    // Call codes.
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER = 1000;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_OK = 1001;
    public final static int CREATE_GOOGLEDRIVE_BACKUP_FOLDER_RESULT_ALREADY_CREATED = 1002;

    // Names.
    public final static String GOOGLE_DRIVE_FOLDER_NAME = "GOOGLE_DRIVE_FOLDER_NAME";

    // DEFAULT VALUES.
    public final static int SPOTIFY_DEFAULT_INDEX_PLAYLIST = 0;
}
