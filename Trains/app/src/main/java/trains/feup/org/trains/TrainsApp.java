package trains.feup.org.trains;

import android.app.Application;
import android.content.Context;

/**
 * Created by mzamith on 17/03/17.
 */

public class TrainsApp extends Application {

    private static TrainsApp instance;

    public static TrainsApp getInstance() {
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
