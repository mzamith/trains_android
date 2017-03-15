package trains.feup.org.trains.api;

import org.json.JSONObject;

/**
 * Created by mzamith on 15/03/17.
 */

public interface ServerCallback {

    public static String CONFLICT = "409";
    public static String OK = "200";
    public static String INTERNAL_SERVER_ERROR = "500";

    void OnSuccess(JSONObject result);

    //TODO
    //Add On Error Callback

    void OnError (String errorCode);
}
