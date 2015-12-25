package com.gagauz.tracker.db.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "feature")
public class Feature extends TimeTrackedEntity {
    private Project project;
    private User creator;
    private List<FeatureVersion> featureVersions;
    private String name;
    private String description;

    @ForeignKey(name = "fk_feature_project")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ForeignKey(name = "fk_feature_owner")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.feature")
    public List<FeatureVersion> getFeatureVersions() {
        return featureVersions;
    }

    public void setFeatureVersions(List<FeatureVersion> featureVersions) {
        this.featureVersions = featureVersions;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Feature<id=" + getId() + ">";
    }
}
