package com.gagauz.tracker.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class Project {

    private int id;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
