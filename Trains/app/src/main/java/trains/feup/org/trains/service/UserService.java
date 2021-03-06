package trains.feup.org.trains.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import trains.feup.org.trains.LoginActivity;
import trains.feup.org.trains.R;
import trains.feup.org.trains.api.ApiInvoker;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Account;
import trains.feup.org.trains.api.ApiEndpoint;
import trains.feup.org.trains.model.Credentials;
import trains.feup.org.trains.model.SimpleAccount;
import trains.feup.org.trains.util.JsonUtil;

/**
 * Created by mzamith on 15/03/17.
 */

public class UserService extends Service{

    public UserService() {
        super();
    }

    public JsonObjectRequest register(Context context, String username, String password, String cardNumber, Date cardDate, final ServerObjectCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "/register";

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject postBody = buildAccount(username, password, cardNumber, cardDate);

        JsonObjectRequest postRequest = ApiInvoker.post(url, postBody, null, callback);

        queue.add(postRequest);
        return postRequest;
    }

    public void login(Context context, String username, String password, final ServerObjectCallback callback){

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postBody = this.buildCredentials(username, password);

        JsonObjectRequest loginRequest = ApiInvoker.login(postBody, callback);

        queue.add(loginRequest);

    }

    public void logout(){

        preferences.edit().remove(context.getString(R.string.saved_token)).commit();
        preferences.edit().remove(context.getString(R.string.saved_username)).commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void logoutWithExtra(int extra){

        preferences.edit().remove(context.getString(R.string.saved_token)).commit();
        preferences.edit().remove(context.getString(R.string.saved_username)).commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(context.getString(R.string.error_connection), extra);
        context.startActivity(intent);
    }

    public JsonObjectRequest getProfile(Context context, final ServerObjectCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "/api/profile";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest getRequest = ApiInvoker.get(url, token, callback);

        queue.add(getRequest);
        return getRequest;
    }

    public JsonObjectRequest updateProfile(Context context, SimpleAccount account, final ServerObjectCallback callback) {

        String url = ApiEndpoint.getEndpoint() + "/api/profile";

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject putBody = buildSimpleAccount(account);

        JsonObjectRequest putRequest = ApiInvoker.put(url, putBody, token, callback);

        queue.add(putRequest);
        return putRequest;
    }


    private JSONObject buildAccount(String username, String password, String cardNumber, Date cardDate){
        try{
            JSONObject postBody = new JSONObject(JsonUtil.serialize(new Account(username, password, cardNumber, cardDate)));
            return postBody;

        }catch (JSONException e){
            Log.e("Exception in Service", "Error serializing Account");
            return null;
        }
    }

    private JSONObject buildSimpleAccount(SimpleAccount account){
        try{
            JSONObject postBody = new JSONObject(JsonUtil.serialize(account));
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
