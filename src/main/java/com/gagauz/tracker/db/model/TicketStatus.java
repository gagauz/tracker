package com.gagauz.tracker.db.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ticket_status")
public class TicketStatus extends BaseEntity {
    private Project project;
    private String name;
    private String css;
    private String description;
    private Collection<TicketStatus> allowedTo = new ArrayList<>();
    private Set<RoleGroup> approvers;
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

    @OneToMany(fetch = FetchType.LAZY)
    public Set<RoleGroup> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<RoleGroup> approvers) {
        this.approvers = approvers;
    }


    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}
