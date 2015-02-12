package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

@MappedSuperclass
public class AbstractStageAction implements Identifiable {
    private int id;
    private Stage owner;

    @Id
    @GeneratedValue
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Stage getOwner() {
        return owner;
    }

    public void setOwner(Stage owner) {
        this.owner = owner;
    }
}
