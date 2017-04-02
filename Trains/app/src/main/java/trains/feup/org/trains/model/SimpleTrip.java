package trains.feup.org.trains.model;

import java.io.Serializable;
import java.util.Date;

public class SimpleTrip implements Serializable {
	
	private Long departureTime;
	private Station departureStation;
	
	public SimpleTrip(){}
	
	public SimpleTrip(Long departureTime, Station departureStation) {
		this.departureTime = departureTime;
		this.departureStation = departureStation;
	}

	public Long getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Long departureTime) {
		this.departureTime = departureTime;
	}

	public Station getDepartureStation() {
		return departureStation;
	}

	public void setDepartureStation(Station departureStation) {
		this.departureStation = departureStation;
	}

	public String getTimeString(){
		return String.format("%02d", new Date(this.departureTime).getHours()) + ":" + String.format("%02d", new Date(this.departureTime).getMinutes());
	}

}
