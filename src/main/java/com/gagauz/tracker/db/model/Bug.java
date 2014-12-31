package com.gagauz.tracker.db.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "bug")
public class Bug implements Identifiable {
    private int id;
    private FeatureVersion featureVersion;
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

    @JoinColumns({
            @JoinColumn(name = "feature_id", updatable = false, referencedColumnName = "feature_id"),
            @JoinColumn(name = "version_id", updatable = false, referencedColumnName = "version_id")
    })
    @ManyToOne(fetch = FetchType.LAZY)
    public FeatureVersion getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(FeatureVersion featureVersion) {
        this.featureVersion = featureVersion;
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
