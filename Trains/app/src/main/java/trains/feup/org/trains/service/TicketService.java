package trains.feup.org.trains.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import trains.feup.org.trains.api.ApiEndpoint;
import trains.feup.org.trains.api.ApiInvoker;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.util.JsonUtil;

/**
 * Created by mzamith on 01/04/17.
 */

public class TicketService extends Service {

    private static TicketService instance = null;

    private TicketService() {
        super();
    }

    public static TicketService getInstance(){

        return (instance == null) ? new TicketService() : instance;

    }

    public JsonObjectRequest buyTicket(Context context, Ticket ticket, final ServerObjectCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "/api/ticket";
        JsonObjectRequest postRequest = null;

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject postBody = new JSONObject(JsonUtil.serialize(ticket).toString());
            postRequest = ApiInvoker.post(url, postBody, token, callback);
            queue.add(postRequest);

        }catch (JSONException je){
            Log.e("ERROR", je.getStackTrace().toString());
            callback.OnError(500);
        }

        return postRequest;
    }
}
