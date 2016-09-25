package com.gagauz.tracker.db.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "name"})
})
public class Version extends TimeTrackedEntity {

    private Project project;
    private String name;
    private String branch;
    private List<FeatureVersion> featureVersion;
    private Date releaseDate = new Date();
    private boolean released = false;

    public Version() {
    }

    public Version(int id) {
        setId(id);
    }

    @ForeignKey(name = "fk_version_project")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @ForeignKey(name = "fk_version_features")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.version")
    public List<FeatureVersion> getFeatureVersions() {
        return featureVersion;
    }

    public void setFeatureVersions(List<FeatureVersion> featureVersion) {
        this.featureVersion = featureVersion;
    }

    @Column
    @Temporal(TemporalType.DATE)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column
    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    @Override
    public String toString() {
        return "Version<id=" + getId() + ">";
    }
}
