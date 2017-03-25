package org.feup.trains.model;

import java.util.Date;
import java.util.List;

public class Travel {
	
	private Station from;
	private Station to;
	
	private Line line;
	
	private Train train;
	
	private List<SimpleTripDTO> trips;
	
	private int totalDuration;
	private int totalDistance;
	
	private Long price;
	
	private Date startTime;
	private Date endTime;
	
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
	public List<SimpleTripDTO> getTrips() {
		return trips;
	}
	public void setTrips(List<SimpleTripDTO> trips) {
		this.trips = trips;
	}
	public int getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	

}
