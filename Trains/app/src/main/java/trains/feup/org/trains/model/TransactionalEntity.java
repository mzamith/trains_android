
package trains.feup.org.trains.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


/**
 * The parent class for all transactional persistent entities.
 * 
 * @author Manuel Zamith
 */
public class TransactionalEntity implements Serializable {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The primary key identifier.
     */
    private Long id;

    /**
     * A secondary unique identifier which may be used as a reference to this
     * entity by external systems.
     */
    private String referenceId = UUID.randomUUID().toString();

    /**
     * The entity instance version used for optimistic locking.
     */
    private Integer version;

    /**
     * A reference to the entity or process which created this entity instance.
     */
    private String createdBy;

    /**
     * The timestamp when this entity instance was created.
     */
    private Date createdAt;

    /**
     * A reference to the entity or process which most recently updated this
     * entity instance.
     */
    private String updatedBy;

    /**
     * The timestamp when this entity instance was most recently updated.
     */
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    /**
     * Determines the equality of two TransactionalEntity objects. If the
     * supplied object is null, returns false. If both objects are of the same
     * class, and their <code>id</code> values are populated and equal, return
     * <code>true</code>. Otherwise, return <code>false</code>.
     * 
     * @param that An Object
     * @return A boolean
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this.getClass().equals(that.getClass())) {
            TransactionalEntity thatTE = (TransactionalEntity) that;
            if (this.getId() == null || thatTE.getId() == null) {
                return false;
            }
            if (this.getId().equals(thatTE.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the hash value of this object.
     * 
     * @return An int
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        if (getId() == null) {
            return -1;
        }
        return getId().hashCode();
    }
}