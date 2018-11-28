package com.gagauz.tracker.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.gagauz.tracker.db.base.DB;

@Entity
@Table(name = "version", uniqueConstraints = {
        @UniqueConstraint(columnNames = { DB.Column.project_id, "name" })
})
public class Version extends TimeTrackedEntity implements Comparable<Version> {

    private Project project;
    private String name;
    private Date releaseDate = new Date();
    private boolean released = false;

    public Version() {
        // default constructor
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = DB.Column.project_id)
    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Temporal(TemporalType.DATE)
    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column
    public boolean isReleased() {
        return this.released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    @Override
    public int compareTo(Version o) {
        return getName().compareTo(o.getName());
    }
}
