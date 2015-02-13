package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Child;
import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

@Entity
@Table(name = "stage_trigger")
public class StageTrigger implements Identifiable, Child<Stage> {
    private int id;
    private Stage parent;
    private String name;
    private String data;
    private String cron;
    private boolean enabled = true;
    private Type type;

    @Id
    @GeneratedValue
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Stage getParent() {
        return parent;
    }

    @Override
    public void setParent(Stage parent) {
        this.parent = parent;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(nullable = true)
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Column
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    public enum Type {
        SCRIPT;
    }
}
