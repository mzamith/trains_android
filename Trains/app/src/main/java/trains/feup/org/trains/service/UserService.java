package trains.feup.org.trains.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import trains.feup.org.trains.R;
import trains.feup.org.trains.TrainsApp;
import trains.feup.org.trains.api.ApiInvoker;
import trains.feup.org.trains.api.ServerCallback;
import trains.feup.org.trains.model.Account;
import trains.feup.org.trains.api.ApiEndpoint;
import trains.feup.org.trains.model.Credentials;
import trains.feup.org.trains.util.JsonUtil;

/**
 * Created by mzamith on 15/03/17.
 */

public class UserService {

    SharedPreferences preferences;
    String token;

    public UserService() {
        Context context = TrainsApp.getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        token = preferences.getString(context.getString(R.string.saved_token), "");
    }

    public JsonObjectRequest register(Context context, String username, String password, final ServerCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "register";

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject postBody = buildAccount(username, password);

        JsonObjectRequest postRequest = ApiInvoker.post(url, postBody, null, callback);

        queue.add(postRequest);
        return postRequest;
    }

    public void login(Context context, String username, String password, final ServerCallback callback){

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postBody = this.buildCredentials(username, password);

        JsonObjectRequest loginRequest = ApiInvoker.login(postBody, callback);

        queue.add(loginRequest);

    }

    private JSONObject buildAccount(String username, String password){
        try{
            JSONObject postBody = new JSONObject(JsonUtil.serialize(new Account(username, password)));
            return postBody;

        }catch (JSONException e){
            Log.e("Exception in Service", "Error serializing Account");
            return null;
        }
    }

    private JSONObject buildCredentials(String username, String password){
        try{
            JSONObject postBody = new JSONObject(JsonUtil.serialize(new Credentials(username, password)));
            return postBody;

        }catch (JSONException e){
            Log.e("Exception in Service", "Error serializing Credentials");
            return null;
        }
    }

}
