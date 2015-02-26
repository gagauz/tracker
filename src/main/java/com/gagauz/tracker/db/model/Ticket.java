package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.ArrayListType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ticket")
@TypeDefs({
        @TypeDef(name = "listOf.Attachment",
                typeClass = ArrayListType.class,
                parameters = {
                        @Parameter(name = com.gagauz.tracker.db.base.CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
                        @Parameter(name = com.gagauz.tracker.db.base.CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
                }
        )
})
public class Ticket implements Identifiable {
    private int id;
    private TicketType type;
    private TicketStatus status;
    private FeatureVersion featureVersion;
    private User author;
    private User owner;
    private Date created = new Date();
    private Date updated;
    private String summary;
    private String description;
    private String cvsVersion;
    private int estimate = 0;
    private int progress = 0;
    private int priority = 0;

    private List<Stage> stages;
    private List<Attachment> attachments;

    @Override
    @Id
    @SequenceGenerator(name = "id_sequence", sequenceName = "ticket_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_sequence")
    @Column(unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    @ForeignKey(name = "fk_ticket_status")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @ForeignKey(name = "fk_ticket_featureVersion")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public FeatureVersion getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(FeatureVersion featureVersion) {
        this.featureVersion = featureVersion;
        if (featureVersion != null) {
            cvsVersion = featureVersion.getFeature().getProject().getCurrentCvsVersion();
        }
    }

    //    @ForeignKey(name = "fk_ticket_feature")
    //    @JoinColumn(nullable = false)
    //    @ManyToOne(fetch = FetchType.LAZY)
    @Transient
    public Feature getFeature() {
        return featureVersion.getFeature();
    }

    //    public void setFeature(Feature feature) {
    //        this.feature = feature;
    //    }

    //    @ForeignKey(name = "fk_ticket_version")
    //    @JoinColumn(nullable = true)
    //    @ManyToOne(fetch = FetchType.LAZY)
    @Transient
    public Version getVersion() {
        return featureVersion.getVersion();
    }

    //    public void setVersion(Version version) {
    //        this.version = version;
    //    }

    @ForeignKey(name = "fk_ticket_author")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ForeignKey(name = "fk_ticket_owner")
    @JoinColumn(nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column(nullable = false)
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
    public String getCvsVersion() {
        return cvsVersion;
    }

    public void setCvsVersion(String cvsVersion) {
        this.cvsVersion = cvsVersion;
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

    @Column(nullable = false)
    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    @Column(nullable = false)
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Column(nullable = false)
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @JoinTable(name = "stage_tickets")
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    @Column(columnDefinition = "TEXT")
    @Type(type = "listOf.Attachment")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Transient
    public boolean isBug() {
        return type == TicketType.BUG;
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
