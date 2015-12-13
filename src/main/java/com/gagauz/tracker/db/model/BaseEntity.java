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
        return (this == obj) || (obj != null && (((BaseEntity) obj).id == id));
    }

    @Override
    public int hashCode() {
        return id;
    }

}
