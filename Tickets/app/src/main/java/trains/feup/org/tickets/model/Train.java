/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trains.feup.org.tickets.model;

/**
 * The Train class is an entity model object. It represents a train, with a
 * passenger capacity.
 *
 * @author Renato Ayres
 */

public class Train extends ReferenceEntity {

    /**
     * Passenger capacity of the train.
     */
    private Long capacity;

    public Train() {
	super();
    }

    public Long getCapacity() {
	return capacity;
    }

    public void setCapacity(Long capacity) {
	if (capacity < 1) {
	    throw new IllegalArgumentException("Capacity of the train must be greater than 0.");
	}
	this.capacity = capacity;
    }
}
