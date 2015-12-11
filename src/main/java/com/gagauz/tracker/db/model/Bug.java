package com.gagauz.tracker.db.model;

import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("ticket_id is null")
@DiscriminatorValue("false")
public class Bug extends BaseTicket {
    private Ticket ticket;

    @ForeignKey(name = "fk_bug_ticket")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    @Transient
    public String getType() {
        return "BUG";
    }
}
