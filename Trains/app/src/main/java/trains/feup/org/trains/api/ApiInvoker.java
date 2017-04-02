package trains.feup.org.trains.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
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

    public static JsonObjectRequest post(String url, JSONObject body, final String token, final ServerObjectCallback callback) {

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, body,
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

                        if (error instanceof NoConnectionError){
                            callback.OnError(ServerObjectCallback.NOT_FOUND);
                        }

                        else if (error instanceof ServerError && response != null) {
                            try {

                               callback.OnError(response.statusCode);


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
                        }else{
                            callback.OnError(response.statusCode);
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
            };
        };
        return postRequest;
    };

    public static JsonArrayRequest getList(String url, final String token, final ServerListCallback callback) {

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

                        if (error instanceof NoConnectionError){
                            callback.OnError(ServerObjectCallback.NOT_FOUND);
                        }

                        else if (error instanceof ServerError && response != null) {
                            try {

                                callback.OnError(response.statusCode);


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
                        }else{
                            callback.OnError(response.statusCode);
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

        return getRequest;
    }

    public static JsonObjectRequest get(String url, final String token, final ServerObjectCallback callback) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
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

                        if (error instanceof NoConnectionError){
                            callback.OnError(ServerObjectCallback.NOT_FOUND);
                        }

                        else if (error instanceof ServerError && response != null) {
                            try {

                                callback.OnError(response.statusCode);


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
                        }else{
                            callback.OnError(response.statusCode);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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

        return getRequest;
    }

    public static JsonObjectRequest put(String url, JSONObject body, final String token, final ServerObjectCallback callback) {

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, body,
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

                        if (error instanceof NoConnectionError){
                            callback.OnError(ServerObjectCallback.NOT_FOUND);
                        }

                        else if (error instanceof ServerError && response != null) {
                            try {

                                callback.OnError(response.statusCode);


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
                        }else{
                            callback.OnError(response.statusCode);
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
            };
        };
        return putRequest;
    };

    public static JsonObjectRequest login(JSONObject body, final ServerObjectCallback callback){

        String url = ApiEndpoint.getEndpoint() + "/login";

        LoginRequest postRequest = new LoginRequest(Request.Method.POST, url, body,
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

                        if (error instanceof NoConnectionError){
                            callback.OnError(ServerObjectCallback.NOT_FOUND);
                        }

                        else if (error instanceof ServerError && response != null) {
                            try {

                                callback.OnError(response.statusCode);

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
                        }else{
                            callback.OnError(response.statusCode);
                        }
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            };
        };
        return postRequest;
    }

}
