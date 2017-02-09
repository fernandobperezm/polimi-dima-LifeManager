package fernandoperez.lifemanager.googleapi.interfaces;

import java.util.List;

import fernandoperez.lifemanager.models.Email;

/**
 * Created by fernandoperez on 2/9/17.
 */

public interface OnBackgroundTaskListener {
    void onTaskCompleted(List<Email> emailList);
}
