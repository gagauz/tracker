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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gagauz.tracker.db.base.ArrayListType;

@Entity
@Table(name = "ticket")
@TypeDefs({
        @TypeDef(name = "listOf.Attachment", typeClass = ArrayListType.class, parameters = {
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
        })
})
public class Ticket extends TimeTrackedEntity {
    private FeatureVersion featureVersion;
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

    @ForeignKey(name = "fk_ticket_featureVersion")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public FeatureVersion getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(FeatureVersion featureVersion) {
        this.featureVersion = featureVersion;
    }

    @ForeignKey(name = "fk_ticket_status")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
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

    @Transient
    public String getType() {
        return null;
    }

    @ForeignKey(name = "fk_ticket_parent")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public Ticket getParent() {
        return parent;
    }

    public void setParent(Ticket ticket) {
        this.parent = ticket;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public List<Ticket> getChildren() {
        return children;
    }

    public void setChildren(List<Ticket> children) {
        this.children = children;
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
}
