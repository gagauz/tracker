package com.gagauz.tracker.db.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "workflow")
public class Workflow extends TimeTrackedEntity {
    private Ticket ticket;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private User fromOwner;
    private User toOwner;
    private Ticket subTicket;

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
}
