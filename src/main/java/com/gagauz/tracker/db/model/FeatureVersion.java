package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gagauz.tracker.db.base.ArrayListType;
import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "feature_version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"feature_id", "version_id"})
})
@TypeDefs({
        @TypeDef(name = "listOf.Attachment",
                typeClass = ArrayListType.class,
                parameters = {
                        @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
                        @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
                }
        )
})
public class FeatureVersion implements Identifiable, Serializable {

    private static final long serialVersionUID = -8693198398126115278L;
    private int id;
    private Feature feature;
    private Version version;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    //    private List<Task> tasks;
    private String name;
    private String description;

    private List<Attachment> attachments;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ForeignKey(name = "fk_featureVersion_feature")
    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @ForeignKey(name = "fk_featureVersion_version")
    @JoinColumn(name = "version_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @ForeignKey(name = "fk_featureVersion_creator")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ForeignKey(name = "fk_featureVersion_owner")
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

    //    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureVersion")
    //    public List<Task> getTasks() {
    //        return tasks;
    //    }
    //
    //    public void setTasks(List<Task> tasks) {
    //        this.tasks = tasks;
    //    }

    @Column(columnDefinition = "TEXT")
    @Type(type = "listOf.Attachment")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj) || (obj != null && (((FeatureVersion) obj).id == id));
    }

    @Override
    public int hashCode() {
        return id;
    }
}
