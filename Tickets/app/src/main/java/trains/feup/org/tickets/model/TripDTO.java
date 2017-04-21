/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.tickets.model;


import java.io.Serializable;

/**
 * The TripDTO class is an entity model object. It holds duration (expressed in
 * seconds), distance (expressed in meters) and the designated {@link LineDTO}
 * between two {@link StationDTO}.
 *
 * @author Renato Ayres
 */

public class TripDTO implements Serializable {

    /**
     * The LineDTO in which this trip is part of.
     */
    private LineDTO line;

    /**
     * StationDTO where the train comes from.
     */
    private StationDTO from;

    /**
     * StationDTO where the train goes to.
     */
    private StationDTO to;

    /**
     * Duration of the trip, expressed in seconds.
     */
    private Long duration;

    /**
     * Distance of the trip, expressed in meters.
     */
    private Long distance;

    public TripDTO() {
        super();
    }

    public LineDTO getLine() {
        return line;
    }

    public void setLine(LineDTO line) {
        this.line = line;
    }

    public StationDTO getFrom() {
        return from;
    }

    public void setFrom(StationDTO from) {
        this.from = from;
    }

    public StationDTO getTo() {
        return to;
    }

    public void setTo(StationDTO to) {
        this.to = to;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        if (duration < 1) {
            throw new IllegalArgumentException("Duration must be greater than 0,"
                    + " expressed in seconds.");
        }
        this.duration = duration;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("Distance must be greater than 0,"
                    + " expressed in meters.");
        }
        this.distance = distance;
    }

}
