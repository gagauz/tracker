package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CommitOwner;
import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sub_task")
public class SubTask extends CommitOwner implements Identifiable {
    private int id;
    private Task task;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    private String summary;
    private String description;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumns({
            @JoinColumn(name = "feature_id", updatable = false, referencedColumnName = "feature_id"),
            @JoinColumn(name = "version_id", updatable = false, referencedColumnName = "version_id")
    })
    @ManyToOne(fetch = FetchType.LAZY)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
