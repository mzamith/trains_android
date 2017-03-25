package trains.feup.org.trains.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import trains.feup.org.trains.api.ApiEndpoint;
import trains.feup.org.trains.api.ApiInvoker;
import trains.feup.org.trains.api.ServerListCallback;

/**
 * Created by mzamith on 24/03/17.
 */

public class TripService extends Service {

    public TripService(){
        super();
    }

    public JsonArrayRequest getTravels(Context context, long originIndex, long destinationIndex, final ServerListCallback callback) {

        String url = String.format(ApiEndpoint.getEndpoint() + "/api/travels?origin=%1$d&destination=%2$d", originIndex, destinationIndex);

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest getRequest = ApiInvoker.getList(url, token, callback);

        queue.add(getRequest);
        return getRequest;
    }
}
