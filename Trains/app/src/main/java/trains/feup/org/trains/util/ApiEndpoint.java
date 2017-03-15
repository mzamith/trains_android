package trains.feup.org.trains.util;

/**
 * Created by mzamith on 15/03/17.
 */

public class ApiEndpoint {

    //Add more revelant endpoints;
    private final static String LOCALHOST = "http://10.0.2.2:8080/";
    private final static String PRODUCTION = "whatever";

    public static String getEndpoint(){

        //change here to configure correct endpoint for your use.
        return LOCALHOST;
    }

}
