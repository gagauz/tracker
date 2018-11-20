package com.gagauz.tracker.db.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.xl0e.hibernate.model.IModel;

@MappedSuperclass
public class Model implements IModel<Integer> {
	private Integer id;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof Model)) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		return ((Model) obj).id == id;
	}
}
