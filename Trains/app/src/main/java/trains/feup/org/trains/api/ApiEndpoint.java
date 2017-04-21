package trains.feup.org.trains.api;

/**
 * Created by mzamith on 15/03/17.
 */

public class ApiEndpoint {

    //Add more relevant endpoints;
    private final static String LOCALHOST = "http://192.168.1.67:8080";
    private final static String PRODUCTION = "whatever";

    public static String getEndpoint(){

        //change here to configure correct endpoint for your use.
        return LOCALHOST;
    }

}
