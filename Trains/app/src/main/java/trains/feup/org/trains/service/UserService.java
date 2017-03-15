package trains.feup.org.trains.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import trains.feup.org.trains.api.ServerCallback;
import trains.feup.org.trains.model.Account;
import trains.feup.org.trains.util.ApiEndpoint;
import trains.feup.org.trains.util.JsonUtil;

/**
 * Created by mzamith on 15/03/17.
 */

public class UserService {

    SharedPreferences sharedPreferences;
    String url = ApiEndpoint.getEndpoint() + "/register";
    JSONObject postBody;

    public JsonObjectRequest register(Context context, String username, String password, final ServerCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);
        this.buildBody(username, password);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.OnSuccess(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {

                                if (response.statusCode == 409){
                                    callback.OnError(ServerCallback.CONFLICT);
                                } else {
                                    callback.OnError(ServerCallback.INTERNAL_SERVER_ERROR);
                                }

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);


                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("site", "code");
                params.put("network", "tutsplus");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            };
        };
        queue.add(postRequest);
        return postRequest;
    };

    private void buildBody(String username, String password){
        try{
            postBody = new JSONObject(JsonUtil.serialize(new Account(username, password)));
        }catch (JSONException e){
            Log.e("Exception in Service", "Error serializing Account");
        }
    }

}
