package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stage")
public class Stage implements Identifiable {
    private int id;
    private Project project;
    private Date created = new Date();
    private Date updated;
    private String name;
    private String description;
    private Stage parent;
    private StageTrigger trigger;
    private List<StageAction> actions;

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

    @JoinColumn(nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    public Stage getParent() {
        return parent;
    }

    public void setParent(Stage parent) {
        if (null != parent && parent.hasParent(this)) {
            throw new IllegalStateException("Circular parent reference!");
        }
        this.parent = parent;
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

    @Embedded
    public StageTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(StageTrigger trigger) {
        this.trigger = trigger;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public List<StageAction> getActions() {
        return actions;
    }

    public void setActions(List<StageAction> stageActions) {
        this.actions = stageActions;
    }

    private boolean hasParent(Stage stage) {
        if (this.parent == stage) {
            return true;
        }
        return null != parent ? parent.hasParent(stage) : false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

}
