/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.trains.model;


/**
 * The Trip class is an entity model object. It holds duration (expressed in
 * seconds), distance (expressed in meters) and the designated {@link Line}
 * between two {@link Station}.
 *
 * @author Renato Ayres
 */

public class Trip extends ReferenceEntity {

    /**
     * The Line in which this trip is part of.
     */
    private Line line;

    /**
     * Station where the train comes from.
     */
    private Station from;

    /**
     * Station where the train goes to.
     */
    private Station to;

    /**
     * Duration of the trip, expressed in seconds.
     */
    private Long duration;

    /**
     * Distance of the trip, expressed in meters.
     */
    private Long distance;

    public Trip() {
        super();
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

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
