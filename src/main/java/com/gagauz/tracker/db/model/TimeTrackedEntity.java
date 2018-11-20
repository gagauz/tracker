package com.gagauz.tracker.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gagauz.tracker.db.base.Model;

@MappedSuperclass
public class TimeTrackedEntity extends Model {

	private Date created;
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
}
