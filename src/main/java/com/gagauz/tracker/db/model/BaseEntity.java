package com.gagauz.tracker.db.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.gagauz.tracker.db.base.Identifiable;

@MappedSuperclass
public class BaseEntity implements Identifiable {
    private int id;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass().isAssignableFrom(obj.getClass())) {
            return (((BaseEntity) obj).id == id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getName() + "#" + id;
    }
}
