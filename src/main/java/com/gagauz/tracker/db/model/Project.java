package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

    private int id;
    private String name;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
