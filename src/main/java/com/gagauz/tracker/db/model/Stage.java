package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "stage")
public class Stage implements StageTrigger, Identifiable {
    private int id;
    private Project project;
    private Date created = new Date();
    private Date updated;
    private String name;
    private String description;
    private StageTrigger trigger;

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
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public StageTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(StageTrigger trigger) {
        if (null == trigger) {
            if (null != this.trigger) {
                this.trigger.removeStage(this);
            }
        } else if (null != trigger) {
            trigger.addStage(this);
        }
        this.trigger = trigger;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    @Override
    public void triggerStages() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addStage(Stage stage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeStage(Stage stage) {
        // TODO Auto-generated method stub

    }

}
