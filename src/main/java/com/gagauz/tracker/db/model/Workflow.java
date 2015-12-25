package com.gagauz.tracker.db.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "workflow")
public class Workflow extends TimeTrackedEntity {
    private Ticket ticket;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private User author;
    private User fromOwner;
    private User toOwner;
    private Ticket subTicket;
    private String comment;
    private List<Attachment> attachments;

    @ForeignKey(name = "fk_workflow_tiket")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @ForeignKey(name = "fk_workflow_fstatus")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(TicketStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    @ForeignKey(name = "fk_workflow_tstatus")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(TicketStatus toStatus) {
        this.toStatus = toStatus;
    }

    @ForeignKey(name = "fk_workflow_author")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ForeignKey(name = "fk_workflow_fowner")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public User getFromOwner() {
        return fromOwner;
    }

    public void setFromOwner(User fromOwner) {
        this.fromOwner = fromOwner;
    }

    @ForeignKey(name = "fk_workflow_towner")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public User getToOwner() {
        return toOwner;
    }

    public void setToOwner(User toOwner) {
        this.toOwner = toOwner;
    }

    @ForeignKey(name = "fk_workflow_subticket")
    @OneToOne(optional = true)
    @JoinColumn()
    public Ticket getSubTicket() {
        return subTicket;
    }

    public void setSubTicket(Ticket subTicket) {
        this.subTicket = subTicket;
    }

    @Column
    @Lob
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
