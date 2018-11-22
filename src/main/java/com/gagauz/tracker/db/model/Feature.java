package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.DB;

@Entity
@Table(name = DB.Table.feature)
public class Feature extends TimeTrackedEntity {
    private static final long serialVersionUID = 8492010488178699625L;
    private Project project;
    private User creator;
    private String name;
    private String description;

    public Feature() {
        // default constructor
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Lob
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
