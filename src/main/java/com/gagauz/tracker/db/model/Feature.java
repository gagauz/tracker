package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "feature")
public class Feature implements Identifiable {
    private int id;
    private Project project;
    private User creator;
    private Date created = new Date();
    private Date updated = new Date();
    private List<FeatureVersion> featureVersions;
    private String name;
    private String description;

    @Override
    @Id
    @SequenceGenerator(name = "id_sequence", sequenceName = "feature_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_sequence")
    @Column(unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ForeignKey(name = "fk_feature_project")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ForeignKey(name = "fk_feature_owner")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feature")
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

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof Feature && ((Feature) obj).id == id;
    }

}
