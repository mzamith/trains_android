package trains.feup.org.trains.api;

import org.json.JSONArray;

/**
 * Created by mzamith on 19/03/17.
 */

public interface ServerListCallback {

    int OK = 200;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int TIMEOUT = 408;
    int CONFLICT = 409;
    int INTERNAL_SERVER_ERROR = 500;
    int PRECONDITION_FAILED = 412;
    int NOT_ACCEPTABLE = 406;

    void OnSuccess(JSONArray result);

    //TODO
    //Add On Error Callback

    void OnError (int errorCode);
}
