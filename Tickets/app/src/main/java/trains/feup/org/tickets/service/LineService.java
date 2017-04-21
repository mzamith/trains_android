package trains.feup.org.tickets.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import trains.feup.org.tickets.api.ApiEndpoint;
import trains.feup.org.tickets.api.ApiInvoker;
import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.api.VolleySingleton;

/**
 * Created by Renato on 4/6/2017.
 */

public class LineService extends Service {

    public LineService(Context context) {
        super(context);
    }

    public void getLines(ServerCallback<JSONArray> callback) {

        String url = ApiEndpoint.LINES_ENDPOINT;

        JsonArrayRequest getRequest = ApiInvoker.getList(url, getToken(), callback);

        RequestQueue queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        queue.add(getRequest);

    }

    public void getLineDepartures(long line, ServerCallback<JSONArray> callback) {

        String url = ApiEndpoint.LINES_ENDPOINT + "/" + line + "/departures";

        JsonArrayRequest getRequest = ApiInvoker.getList(url, getToken(), callback);

        RequestQueue queue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        queue.add(getRequest);

    }

}
