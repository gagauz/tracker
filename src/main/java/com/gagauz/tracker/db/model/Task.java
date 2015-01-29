package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task")
public class Task implements Identifiable {
    private int id;
    private TaskType type;
    private TaskStatus status = TaskStatus.OPEN;
    private Feature feature;
    private Version version;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated;
    private String summary;
    private String description;
    private int estimated;
    private int progress;
    private int priority;

    private List<Stage> stages;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @JoinColumn(name = "version_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    //    @JoinColumns({
    //            @JoinColumn(name = "feature_id", insertable = false, updatable = false, referencedColumnName = "feature_id"),
    //            @JoinColumn(name = "version_id", insertable = false, updatable = false, referencedColumnName = "version_id")
    //    })
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    public FeatureVersion getFeatureVersion() {
    //        return featureVersion;
    //    }
    //
    //    public void setFeatureVersion(FeatureVersion featureVersion) {
    //        this.featureVersion = featureVersion;
    //    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JoinColumn(nullable = true)
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

    @Column
    public int getEstimated() {
        return estimated;
    }

    public void setEstimated(int estimated) {
        this.estimated = estimated;
    }

    @Column
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Column
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @JoinTable(name = "stage_tasks")
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    @Transient
    public boolean isBug() {
        return type == TaskType.BUG;
    }

}
