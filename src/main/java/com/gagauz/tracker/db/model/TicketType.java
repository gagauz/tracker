package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ticket_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "project_id", "name" })
})
public class TicketType extends TimeTrackedEntity {
    private String name;
    private Project project;
    private TicketType parent;
    private RoleGroup creator;
    private RoleGroup assignee;
    private String css;

    @Column(nullable = true)
    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public TicketType getParent() {
        return this.parent;
    }

    public void setParent(TicketType parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public RoleGroup getCreator() {
        return creator;
    }

    public void setCreator(RoleGroup creator) {
        this.creator = creator;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public RoleGroup getAssignee() {
        return assignee;
    }

    public void setAssignee(RoleGroup assignee) {
        this.assignee = assignee;
    }


    @Column(nullable = true)
    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

}
