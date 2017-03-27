package trains.feup.org.tickets.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import trains.feup.org.tickets.LoginActivity;
import trains.feup.org.tickets.R;
import trains.feup.org.tickets.api.ApiInvoker;
import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.api.VolleySingleton;
import trains.feup.org.tickets.model.Credentials;
import trains.feup.org.tickets.util.JsonUtil;

/**
 * Created by mzamith on 15/03/17.
 */

public class UserService extends Service {

    public UserService() {
        super();
    }


    public void login(Context context, String username, String password, final ServerCallback callback) {


        JSONObject postBody = this.buildCredentials(username, password);

        JsonObjectRequest loginRequest = ApiInvoker.login(postBody, callback);

        VolleySingleton.getInstance(context).addToRequestQueue(loginRequest);

    }

    public void logout() {

        preferences.edit().remove(context.getString(R.string.saved_token)).commit();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    private JSONObject buildCredentials(String username, String password) {
        try {
            JSONObject postBody = new JSONObject(JsonUtil.serialize(new Credentials(username, password)));
            return postBody;

        } catch (JSONException e) {
            Log.e("Exception in Service", "Error serializing Credentials");
            return null;
        }
    }

}
