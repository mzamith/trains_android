package trains.feup.org.trains.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mzamith on 15/03/17.
 */

public interface ServerObjectCallback {

    public static int OK = 200;
    public static int UNAUTHORIZED = 401;
    public static int FORBIDDEN = 403;
    public static int NOT_FOUND = 404;
    public static int TIMEOUT = 408;
    public static int CONFLICT = 409;
    public static int INTERNAL_SERVER_ERROR = 500;

    void OnSuccess(JSONObject result);

    //TODO
    //Add On Error Callback

    void OnError (int errorCode);
}
