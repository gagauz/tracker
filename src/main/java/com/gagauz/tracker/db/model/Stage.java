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
    private List<StageTrigger> triggers;
    private List<StageAction> beforeActions;
    private List<StageAction> afterActions;

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

    @JoinColumn(nullable = true)
    @OneToMany(fetch = FetchType.LAZY)
    public List<StageTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<StageTrigger> triggers) {
        this.triggers = triggers;
    }

    @JoinColumn(nullable = true)
    @OneToMany(fetch = FetchType.LAZY)
    public List<StageAction> getBeforeActions() {
        return beforeActions;
    }

    public void setBeforeActions(List<StageAction> beforeActions) {
        this.beforeActions = beforeActions;
    }

    @JoinColumn(nullable = true)
    @OneToMany(fetch = FetchType.LAZY)
    public List<StageAction> getAfterActions() {
        return afterActions;
    }

    public void setAfterActions(List<StageAction> afterActions) {
        this.afterActions = afterActions;
    }

    private boolean hasParent(Stage stage) {
        if (this.parent == stage) {
            return true;
        }
        return null != parent ? parent.hasParent(stage) : false;
    }

}
