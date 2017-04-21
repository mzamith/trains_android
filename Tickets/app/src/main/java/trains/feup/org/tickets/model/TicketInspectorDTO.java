package trains.feup.org.tickets.model;

import java.io.Serializable;

/**
 * Created by Renato on 4/6/2017.
 */

public class TicketInspectorDTO implements Serializable {

    private Long ticket;

    private Long departure;

    private String departureLabel;

    private String username;

    public TicketInspectorDTO() {
    }

    public TicketInspectorDTO(Long ticket, Long departure, String departureLabel, String username) {
        this.ticket = ticket;
        this.departure = departure;
        this.departureLabel = departureLabel;
        this.username = username;
    }

    public TicketInspectorDTO(TicketDTO ticket, String username) {
        this(ticket.getId(),
                ticket.getDeparture().getId(), ticket.getDeparture().getLabel(),
                username);
    }

    public Long getTicket() {
        return ticket;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    public Long getDeparture() {
        return departure;
    }

    public void setDeparture(Long departure) {
        this.departure = departure;
    }

    public String getDepartureLabel() {
        return departureLabel;
    }

    public void setDepartureLabel(String departureLabel) {
        this.departureLabel = departureLabel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
