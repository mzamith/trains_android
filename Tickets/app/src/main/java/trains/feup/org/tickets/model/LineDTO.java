/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.tickets.model;


import java.io.Serializable;

/**
 * The LineDTO class is an entity model object. It represents a path of train
 * stops.
 *
 * @author Renato Ayres
 */

public class LineDTO implements Serializable {

    private Long id;

    private String label;

    private String code;

    public LineDTO() {
        super();
    }

    private LineDTO(final Long id, final String label, final String code) {
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

    @Override
    public String toString() {
        return String.format("%s - %s", getCode(), getLabel());
    }
}
