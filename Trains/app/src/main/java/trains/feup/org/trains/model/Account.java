
package trains.feup.org.trains.model;

import java.util.Date;
import java.util.Set;
import trains.feup.org.trains.model.Role;

import trains.feup.org.trains.model.TransactionalEntity;

/**
 * The Account class is an entity model object. An Account describes the
 * security credentials and authentication flags that permit access to
 * application functionality.
 * 
 * @author Manuel Zamith
 */
public class Account extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    public Account() {}

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, String cardNumber, Date cardDate) {
        this.username = username;
        this.password = password;
        this.cardNumber = cardNumber;
        this.cardDate = cardDate;
    }

    /**
     * The primary key identifier.
     */

    private String username;

    private String password;

    private boolean enabled = true;

    private boolean credentialsexpired = false;

    private boolean expired = false;

    private boolean locked = false;

    private String cardNumber;

    private Date cardDate;

    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCredentialsexpired() {
        return credentialsexpired;
    }

    public void setCredentialsexpired(boolean credentialsexpired) {
        this.credentialsexpired = credentialsexpired;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCardDate() {
        return cardDate;
    }

    public void setCardDate(Date cardDate) {
        this.cardDate = cardDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}