package com.gagauz.tracker.db.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "version"})
})
public class Version implements Identifiable {

    private int id;
    private Project project;
    private Date created = new Date();
    private Date updated = new Date();
    private String version;
    private List<FeatureVersion> featureVersion;
    private Date releaseDate = new Date();
    private boolean released = false;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ForeignKey(name = "fk_version_project")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @ForeignKey(name = "fk_version_features")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "version")
    public List<FeatureVersion> getFeatureVersions() {
        return featureVersion;
    }

    public void setFeatureVersions(List<FeatureVersion> featureVersion) {
        this.featureVersion = featureVersion;
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

}
