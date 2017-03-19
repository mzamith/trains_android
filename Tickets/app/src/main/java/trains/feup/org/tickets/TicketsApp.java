package trains.feup.org.tickets;

import android.app.Application;
import android.content.Context;

/**
 * Created by mzamith on 17/03/17.
 */

public class TicketsApp extends Application {

    private static TicketsApp instance;

    public static TicketsApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

}
