package trains.feup.org.tickets.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mzamith on 17/03/17.
 */
public class ApiInvoker {

    private static final String TAG = ApiInvoker.class.getName();

    public static JsonObjectRequest get(final String url, final String token, final ServerCallback<JSONObject> callback) {

        Log.i(TAG, "get");

        //Sending null as the jsonRequest makes the request GET
        return new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse");
                        Log.i(TAG, "Response: " + response);
                        try {
                            callback.onSuccess(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse");
                        Log.e(TAG, "Error: " + error);

                        NetworkResponse response = error.networkResponse;
                        if (response != null) {

                            if (error instanceof ServerError) {
                                try {

                                    callback.onError(response.statusCode);

                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);


                                } catch (UnsupportedEncodingException | JSONException e) {
                                    // Couldn't properly decode data to string
                                    e.printStackTrace();
                                }
                            } else {
                                callback.onError(response.statusCode);
                            }
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                if (token != null) {
                    params.put("Authorization", token);
                }

                return params;
            }
        };
    }

    public static JsonArrayRequest getList(final String url, final String token, final ServerCallback<JSONArray> callback) {

        Log.i(TAG, "getMany");

        //Sending null as the jsonRequest makes the request GET
        return new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i(TAG, "onResponse");
                            Log.i(TAG, "Response: " + response);
                            callback.onSuccess(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse");
                        Log.e(TAG, "Error: " + error);

                        NetworkResponse response = error.networkResponse;
                        if (response != null) {

                            if (error instanceof ServerError) {
                                try {

                                    callback.onError(response.statusCode);

                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);


                                } catch (UnsupportedEncodingException | JSONException e) {
                                    // Couldn't properly decode data to string
                                    e.printStackTrace();
                                }
                            } else {
                                callback.onError(response.statusCode);
                            }
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");

                if (token != null) {
                    params.put("Authorization", token);
                }

                return params;
            }
        };
    }

    public static JsonObjectRequest post(String url, JSONObject body, final String token, final ServerCallback callback) {

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);

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

                                callback.onError(response.statusCode);


                                //This is just for debbuging.
                                //I was having trouble trying to figure out ServerError

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
                        } else {
                            callback.onError(response.statusCode);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                //params.put("site", "code");
                //params.put("network", "tutsplus");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");

                if (token != null) params.put("Authorization", token);

                return params;
            }
        };
        return postRequest;
    }

    public static JsonObjectRequest login(JSONObject body, final ServerCallback callback) {

        LoginRequest postRequest = new LoginRequest(Request.Method.POST, ApiEndpoint.LOGIN_ENDPOINT, body,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);

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

                                callback.onError(response.statusCode);

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
                        } else {
                            callback.onError(response.statusCode);
                        }
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        return postRequest;
    }
}
