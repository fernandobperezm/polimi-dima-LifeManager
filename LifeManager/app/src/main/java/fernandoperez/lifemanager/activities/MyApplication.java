package fernandoperez.lifemanager.activities;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

// Twitter imports.
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.greenrobot.greendao.database.Database;

import fernandoperez.lifemanager.helpers.DBHelper;
import fernandoperez.lifemanager.models.DaoMaster;
import fernandoperez.lifemanager.models.DaoSession;
import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fernando on 23/01/2017.
 */

public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TTgmuM3YsUmFekOEu8ACrj5qZ";
    private static final String TWITTER_SECRET = "sFGLKbi3Yd1kaxCjD2H4RfagmANUIprUxEjlIwUcXHfTJHtMvq";

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        printHashKey();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        DBHelper.insertServices(daoSession.getServicesDao());
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "fernandoperez.lifemanager",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("LIFEMANAGER:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}