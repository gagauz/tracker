package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gagauz.hibernate.types.ArrayListType;
import org.gagauz.hibernate.types.CollectionType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Entity
@Table(name = "ticket")
@TypeDefs({
    @TypeDef(name = "listOf.Attachment", typeClass = ArrayListType.class, parameters = {
            @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
            @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
    })
})
public class Ticket extends TimeTrackedEntity {
    private FeatureVersion featureVersion;
    private String key1;
    private TicketType type;
    private TicketStatus status;
    private User author;
    private User owner;
    private String summary;
    private String description;
    private int estimate = 0;
    private int progress = 0;
    private List<Attachment> attachments;
    private Ticket parent;
    private List<Ticket> children;
    private List<Workflow> workflow;

    @ForeignKey(name = "fk_ticket_featureVersion")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public FeatureVersion getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(FeatureVersion featureVersion) {
        this.featureVersion = featureVersion;
        if (null == key1 && null != featureVersion) {
            key1 = featureVersion.getFeature().getProject().getCode() + '-';
        }
    }

    @Column(updatable = false)
    public String getKey1() {
        return key1;
    }

    public void setKey1(String key) {
        key1 = key;
    }

    @ForeignKey(name = "fk_ticket_type")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    @ForeignKey(name = "fk_ticket_status")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @ForeignKey(name = "fk_ticket_author")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ForeignKey(name = "fk_ticket_owner")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
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

    @Column
    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column(columnDefinition = "TEXT")
    @Type(type = "listOf.Attachment")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @ForeignKey(name = "fk_ticket_parent")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public Ticket getParent() {
        return parent;
    }

    public void setParent(Ticket ticket) {
        parent = ticket;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public List<Ticket> getChildren() {
        return children;
    }

    public void setChildren(List<Ticket> children) {
        this.children = children;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    public List<Workflow> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<Workflow> workflow) {
        this.workflow = workflow;
    }

    @Transient
    public Project getProject() {
        return featureVersion.getProject();
    }

    @Transient
    public Feature getFeature() {
        return featureVersion.getFeature();
    }

    @Transient
    public Version getVersion() {
        return featureVersion.getVersion();
    }

    @Transient
    public String getKey() {
        return key1 + getId();
    }
}
