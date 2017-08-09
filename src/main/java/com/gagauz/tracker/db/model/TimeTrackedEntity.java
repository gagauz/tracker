package com.gagauz.tracker.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xl0e.hibernate.model.Model;

@SuppressWarnings("serial")
@MappedSuperclass
public class TimeTrackedEntity extends Model {

    private Date created = new Date();
    private Date updated;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(columnDefinition = "timestamp null on update current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

}
