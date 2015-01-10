package com.gagauz.tracker.db.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "task")
public class Task implements Identifiable {
    private int id;
    //    private FeatureVersion featureVersion;
    private TaskType type;
    private Feature feature;
    private Version version;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    private String summary;
    private String description;
    private int estimated;
    private int progress;
    private List<Commit> commits;

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

    @OneToMany
    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

}
