package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CommitOwner;

import javax.persistence.*;

@Entity
@Table(name = "task")
public class Task extends CommitOwner {
    private int id;
    private Version version;
    private User creator;
    private User owner;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version project) {
        this.version = project;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
