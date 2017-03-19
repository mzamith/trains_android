package trains.feup.org.trains.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import trains.feup.org.trains.R;
import trains.feup.org.trains.TrainsApp;

/**
 * Created by mzamith on 18/03/17.
 */

public abstract class Service {

    protected Context context;
    protected SharedPreferences preferences;
    protected String token;

    public Service() {
        this.context = TrainsApp.getContext();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        this.token = preferences.getString(context.getString(R.string.saved_token), "");
    }
}
