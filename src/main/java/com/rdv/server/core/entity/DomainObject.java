package com.rdv.server.core.entity;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author davidgarcia
 */
@MappedSuperclass
public class DomainObject implements Serializable {

    @Column(name = "last_modification_date", nullable = true)
    @XmlTransient
    private OffsetDateTime lastModificationDate;

    @Basic
    @Column(name = "last_modification_user", nullable = true, length = 20)
    @XmlTransient
    private String lastModificationUser;

    public DomainObject() {
    }


    public OffsetDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * Sets the lastModificationDate
     *
     * @param lastModificationDate The lastModificationDate to set
     */
    public void setLastModificationDate(OffsetDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getLastModificationUser() {
        return lastModificationUser;
    }

    /**
     * Sets the lastModificationUser
     *
     * @param lastModificationUser The lastModificationUser to set
     */
    public void setLastModificationUser(String lastModificationUser) {
        this.lastModificationUser = lastModificationUser;
    }

}
