package trains.feup.org.trains.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Travel implements Serializable {
	
	private Station from;
	private Station to;

	private Departure departure;
	
	private Line line;
	
	private Train train;
	
	private List<SimpleTrip> trips;
	
	private int totalDuration;
	private int totalDistance;
	
	private Long price;
	
	private Long startTime;
	private Long endTime;
	
	public Travel() {}
	
	
	public Station getFrom() {
		return from;
	}

	public void setFrom(Station from) {
		this.from = from;
	}

	public Station getTo() {
		return to;
	}

	public void setTo(Station to) {
		this.to = to;
	}

	public Departure getDeparture() {
		return departure;
	}

	public void setDeparture(Departure departure) {
		this.departure = departure;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public List<SimpleTrip> getTrips() {
		return trips;
	}

	public void setTrips(List<SimpleTrip> trips) {
		this.trips = trips;
	}

	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

    public String getStartTimeString(){

		Date date = new Date(startTime);
        return String.format("%02d", new Date(this.startTime).getHours()) + ":" + String.format("%02d", new Date(this.startTime).getMinutes());
    }

    public String getEndTimeString(){

		Date date = new Date(endTime);

        return String.format("%02d", new Date(this.endTime).getHours()) + ":" + String.format("%02d", new Date(this.endTime).getMinutes());
    }

    public String getDurationString(){

        double conversion = (double) this.totalDuration / 3600.0;
        int hours = (int) conversion;
        int minutes = (int) ( (conversion - (double) hours) * 60.0);

		if (minutes == 0){
			return String.valueOf(hours) + "h";
		} else if (hours == 0){
			return String.valueOf(minutes) + "min";
		} else {
			return String.valueOf(hours) + "h" + String.valueOf(minutes) + "min";
		}
    }

}
