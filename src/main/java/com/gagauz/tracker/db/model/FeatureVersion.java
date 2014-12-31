package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gagauz.tracker.db.base.ArrayListType;
import com.gagauz.tracker.db.base.CollectionType;

@Entity
@Table(name = "feature_version")
@TypeDefs({
        @TypeDef(name = "listOf.Attachment",
                typeClass = ArrayListType.class,
                parameters = {
                        @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
                        @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
                }
        )
})
public class FeatureVersion {

    @Embeddable
    public static class FeatureVersionId implements Serializable {
        private static final long serialVersionUID = 4441697145474451670L;

        private Feature feature;
        private Version version;

        public FeatureVersionId(Feature feature, Version version) {
            setFeature(feature);
            setVersion(version);
        }

        protected FeatureVersionId() {
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

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof FeatureVersionId) {
                return feature.equals(((FeatureVersionId) obj).getFeature()) && version.equals(((FeatureVersionId) obj).getVersion());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return feature.hashCode() * (feature.hashCode() + version.hashCode());
        }

    }

    private FeatureVersionId id = new FeatureVersionId();
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    private List<Task> tasks;
    private List<Bug> bugs;
    private String name;
    private String description;

    private List<Attachment> attachments;

    @EmbeddedId
    public FeatureVersionId getId() {
        return id;
    }

    public void setId(FeatureVersionId id) {
        this.id = id;
    }

    @Transient
    public Feature getFeature() {
        return id.getFeature();
    }

    public void setFeature(Feature feature) {
        this.id.setFeature(feature);
    }

    @Transient
    public Version getVersion() {
        return id.getVersion();
    }

    public void setVersion(Version version) {
        this.id.setVersion(version);
    }

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureVersion")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureVersion")
    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }

    @Column(columnDefinition = "TEXT")
    @Type(type = "listOf.Attachment")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
