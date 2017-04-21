/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.tickets.model;


import java.io.Serializable;

/**
 * The StationDTO class is an entity model object. It represents a train stop.
 *
 * @author Renato Ayres
 */
public class StationDTO implements Serializable {

    private Long id;

    private String label;

    private String code;

    public StationDTO() {
        super();
    }

    public StationDTO(final Long id, final String label, final String code) {
        super();
        this.id = id;
        this.label = label;
        this.code = code;
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

    public String makeName(){
        return this.code + " - " + this.label;
    }
}
