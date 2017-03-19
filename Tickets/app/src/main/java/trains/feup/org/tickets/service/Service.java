package trains.feup.org.tickets.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import trains.feup.org.tickets.R;
import trains.feup.org.tickets.TicketsApp;

/**
 * Created by mzamith on 18/03/17.
 */

public abstract class Service {

    protected Context context;
    protected SharedPreferences preferences;
    protected String token;

    public Service() {
        this.context = TicketsApp.getContext();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        this.token = preferences.getString(context.getString(R.string.saved_token), "");
    }
}
