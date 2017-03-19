
package trains.feup.org.trains.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The parent class for all reference entities (i.e. reference data as opposed
 * to transactional data).
 * 
 * 
 * @author Manuel Zamith
 */
public class ReferenceEntity implements Serializable  {

    private DateFormat dateFormat = new SimpleDateFormat();

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The primary key identifier.
     */
    private Long id;

    /**
     * The unique code value, sometimes used for external reference.
     */
    private String code;

    /**
     * A brief description of the entity.
     */
    private String label;

    /**
     * The ordinal value facilitates sorting the entities.
     */
    private Integer ordinal;

    /**
     * The timestamp at which the entity's values may be applied or used by the
     * system.
     */
    private String effectiveAt;

    /**
     * The timestamp at which the entity's values cease to be used by the
     * system. If <code>null</code> the entity is not expired.
     */
    private String expiresAt;

    /**
     * The timestamp when this entity instance was created.
     */
    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(String effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAtDate(){

        return new Date(Long.parseLong(expiresAt));
    }

    public Date getCreatedAtDate(){

        return new Date(Long.parseLong(createdAt));
    }

    public Date getEffectiveAtDate(){

        return new Date(Long.parseLong(effectiveAt));
    }


}