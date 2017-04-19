/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.trains.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The Ticket class is an entity model object. It represents a ticket, with a
 * passenger capacity.
 *
 * @author Renato Ayres
 */

public class Ticket extends ReferenceEntity {

    /**
     * The departure this ticket is supposed to be used at.
     */
    private Departure departure;

    private Station from;

    /**
     * The station this ticket is allowed to go.
     */
    private Station to;

    /**
     * Price to pay for ticket.
     */
    private double price;

    private String state;

    private long day;

    private String codeDTO;

    public Ticket() {
	    super();
    }

    public Ticket(Travel travel){
        this.departure = travel.getDeparture();
        this.price = travel.getPrice();
        this.to = travel.getTo();
        this.from = travel.getFrom();
    }

    public Departure getDeparture() {
	return departure;
    }

    public void setDeparture(Departure departure) {
	this.departure = departure;
    }

    public Station getTo() {
	return to;
    }

    public void setTo(Station to) {
	this.to = to;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getCodeDTO(){
        return codeDTO;
    }

    public void setCodeDTO(String code){
        codeDTO = code;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "departure=" + departure.getCode() +
                ", to=" + to.getLabel() +
                ", price=" + price +
                '}';
    }
}
