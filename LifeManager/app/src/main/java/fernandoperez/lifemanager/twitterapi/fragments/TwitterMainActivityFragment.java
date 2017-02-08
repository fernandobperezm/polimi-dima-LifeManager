package fernandoperez.lifemanager.twitterapi.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fernandoperez.lifemanager.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TwitterMainActivityFragment extends Fragment {

    public TwitterMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter_main_login, container, false);
    }
}
