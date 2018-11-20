package com.gagauz.tracker.db.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.Model;

@Entity
@Table(name = "ticket_status")
public class TicketStatus extends Model {
    private static final long serialVersionUID = 5968810449492475582L;
    private Project project;
    private String name;
    private String css;
    private String description;
    private Collection<TicketStatus> allowedTo = new ArrayList<>();
    private TicketType ticketType;

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

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    public Collection<TicketStatus> getAllowedTo() {
        return allowedTo;
    }

    public void setAllowedTo(Collection<TicketStatus> allowedTo) {
        this.allowedTo = allowedTo;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}
