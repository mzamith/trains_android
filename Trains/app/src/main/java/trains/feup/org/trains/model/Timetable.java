package trains.feup.org.trains.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mzamith on 05/04/17.
 */

public class Timetable implements Serializable {

    public static final transient int FORWARD = 1;
    public static final transient int BACKWARDS = 2;

    private List<Departure> departures;
    private List<Trip> trips;

    public List<Departure> getDepartures() {
        return departures;
    }
    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }
    public List<Trip> getTrips() {
        return trips;
    }
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
