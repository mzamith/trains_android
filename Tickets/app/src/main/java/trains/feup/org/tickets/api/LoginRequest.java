package trains.feup.org.tickets.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mzamith on 17/03/17.
 */

//Workaround to get the auth header out of the request.

public class LoginRequest extends JsonObjectRequest {


    public LoginRequest(int method, String url, JSONObject jsonRequest, Response.Listener
            <JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public LoginRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            //String jsonString = new String(response.data,
              //      HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("headers", new JSONObject(response.headers));
            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
