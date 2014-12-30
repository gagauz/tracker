package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.ArrayListType;
import com.gagauz.tracker.db.base.CollectionType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

        private int featureId;
        private int versionId;

        public FeatureVersionId(int featureId, int versionId) {
            this.setFeatureId(featureId);
            this.setVersionId(versionId);
        }

        protected FeatureVersionId() {
        }

        public int getFeatureId() {
            return featureId;
        }

        public void setFeatureId(int featureId) {
            this.featureId = featureId;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof FeatureVersionId) {
                return featureId == ((FeatureVersionId) obj).getFeatureId() && versionId == ((FeatureVersionId) obj).getVersionId();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return featureId * (featureId + versionId);
        }

    }

    private FeatureVersionId id = new FeatureVersionId();
    private Feature feature;
    private Version version;
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

    @MapsId("featureId")
    @JoinColumn(nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @MapsId("versionId")
    @JoinColumn(nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version project) {
        this.version = project;
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
