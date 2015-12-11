package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.ArrayListType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@MappedSuperclass
@TypeDefs({
        @TypeDef(name = "listOf.Attachment", typeClass = ArrayListType.class, parameters = {
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Attachment"),
                @Parameter(name = com.gagauz.tracker.db.base.CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.AttachmentSerializer")
        })
})
public abstract class BaseTicket implements Identifiable {
    private int id;
    private TicketStatus status;
    private User author;
    private User owner;
    private Date created = new Date();
    private Date updated;
    private String summary;
    private String description;
    private int estimate = 0;
    private int progress = 0;
    private List<Attachment> attachments;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ForeignKey(name = "fk_ticket_status")
    @JoinColumn(nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

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

    @Column
    @Lob
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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    @Transient
    public abstract String getType();
}
