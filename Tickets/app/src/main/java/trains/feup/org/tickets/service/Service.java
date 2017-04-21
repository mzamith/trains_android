package trains.feup.org.tickets.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import trains.feup.org.tickets.R;

/**
 * Created by mzamith on 18/03/17.
 */

public abstract class Service {

    private Context context;
    private SharedPreferences preferences;
    private String token;

    public Service(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.token = preferences.getString(context.getString(R.string.saved_token), "");
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getToken() {
        return token;
    }
}
