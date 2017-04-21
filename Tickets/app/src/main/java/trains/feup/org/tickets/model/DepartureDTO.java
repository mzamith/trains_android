/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.tickets.model;

import java.io.Serializable;

/**
 * The DepartureDTO class is an entity model object. It holds information about a
 * daily train departure such as time of day, {@link StationDTO} of departure,
 * designated {@link LineDTO} and {@link TrainDTO}.
 *
 * @author Renato Ayres
 */

public class DepartureDTO implements Serializable {

    private Long id;

    private String label;

    private String code;

    private LineDTO line;

    private StationDTO from;

    private TrainDTO train;

    private Long time;

    public DepartureDTO() {
        super();
    }

    private DepartureDTO(final Long id, final String label, final String code, final LineDTO line,
                         final StationDTO from, final TrainDTO train, final Long time) {
        super();
        this.id = id;
        this.label = label;
        this.code = code;
        this.line = line;
        this.from = from;
        this.train = train;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public TrainDTO getTrain() {
        return train;
    }

    public void setTrain(TrainDTO train) {
        this.train = train;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String makeName() {
        return this.label;
    }

    @Override
    public String toString() {
        return makeName();
    }
}
