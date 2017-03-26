package trains.feup.org.tickets.api;

/**
 * Created by mzamith on 15/03/17.
 */

public interface ServerCallback<T> {

    public static int OK = 200;
    public static int UNAUTHORIZED = 401;
    public static int FORBIDDEN = 403;
    public static int NOT_FOUND = 404;
    public static int TIMEOUT = 408;
    public static int CONFLICT = 409;
    public static int INTERNAL_SERVER_ERROR = 500;

    void OnSuccess(T result);

    void OnError(int errorCode);
}
