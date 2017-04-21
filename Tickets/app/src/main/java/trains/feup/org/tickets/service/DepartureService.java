package trains.feup.org.tickets.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.List;

import trains.feup.org.tickets.api.ApiEndpoint;
import trains.feup.org.tickets.api.ApiInvoker;
import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.api.VolleySingleton;

import static android.content.ContentValues.TAG;

/**
 * Created by Renato on 4/6/2017.
 */

public class DepartureService extends Service {

    public DepartureService(Context context) {
        super(context);
    }

    public void getDepartureTickets(long departure, final ServerCallback<JSONArray> callback) {

        String url = ApiEndpoint.PROTECTED_DEPARTURES_ENDPOINT + "/" + departure + "/tickets";

        JsonArrayRequest getRequest = ApiInvoker.getList(url, getToken(), callback);

        RequestQueue queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        queue.add(getRequest);
    }

    public void getDepartures(List<Long> departures, ServerCallback<JSONArray> serverCallback) {

        String url = ApiEndpoint.PROTECTED_DEPARTURES_ENDPOINT + "/" + departures.remove(0);

        Log.i(TAG, "url: " + url);

        for (Long departure : departures) {
            url = url.concat("," + String.valueOf(departure));
        }

        JsonArrayRequest getRequest = ApiInvoker.getList(url, getToken(), serverCallback);

        RequestQueue queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        queue.add(getRequest);


    }
}
