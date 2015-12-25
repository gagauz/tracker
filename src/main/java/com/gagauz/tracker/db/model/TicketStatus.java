package com.gagauz.tracker.db.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "ticket_status")
public class TicketStatus extends BaseEntity {
    private Project project;
    private String name;
    private String description;
    private TicketStatus from;
    private TicketStatus to;
    private Collection<TicketStatus> allowedFrom = new ArrayList<>();
    private Collection<TicketStatus> allowedTo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketStatus getFrom() {
        return from;
    }

    public void setFrom(TicketStatus from) {
        this.from = from;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketStatus getTo() {
        return to;
    }

    public void setTo(TicketStatus to) {
        this.to = to;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "from")
    public Collection<TicketStatus> getAllowedFrom() {
        return allowedFrom;
    }

    public void setAllowedFrom(Collection<TicketStatus> allowedFrom) {
        this.allowedFrom = allowedFrom;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "to")
    public Collection<TicketStatus> getAllowedTo() {
        return allowedTo;
    }

    public void setAllowedTo(Collection<TicketStatus> allowedTo) {
        this.allowedTo = allowedTo;
    }

    @Override
    public String toString() {
        return "TicketStatus<id=" + getId() + ">";
    }
}
