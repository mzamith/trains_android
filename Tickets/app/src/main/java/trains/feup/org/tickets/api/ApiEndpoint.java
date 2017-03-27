package trains.feup.org.tickets.api;

/**
 * Created by mzamith on 15/03/17.
 */
public interface ApiEndpoint {

    //Add more relevant endpoints;
    String ENDPOINT = "http://192.168.2.12:8080";
    String LINES_ENDPOINT = ENDPOINT + "/api/lines";
    String LOGIN_ENDPOINT = ENDPOINT + "/logininspector";
    String DEPARTURES_ENDPOINT = ENDPOINT + "/api/departures";
    String PROTECTED_DEPARTURES_ENDPOINT = ENDPOINT + "/inspector/departures";
    String TICKETS_ENDPOINT = ENDPOINT + "/inspector/tickets";
    String PRODUCTION = "whatever";

}
