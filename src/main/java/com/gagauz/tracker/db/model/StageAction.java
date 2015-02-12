package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stage_action")
public class StageAction implements Identifiable {
    private int id;

    @Id
    @GeneratedValue
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
