package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

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

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
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
