package trains.feup.org.trains.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import trains.feup.org.trains.api.ApiEndpoint;
import trains.feup.org.trains.api.ApiInvoker;
import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;

/**
 * Created by mzamith on 19/03/17.
 */

public class StationService extends Service {

    public StationService() {
        super();
    }

    public JsonArrayRequest getStations(Context context, final ServerListCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "api/stations";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest getRequest = ApiInvoker.getList(url, token, callback);

        queue.add(getRequest);
        return getRequest;
    }
}
