package com.gagauz.tracker.db.model;

import javax.persistence.*;

@Entity
@Table(name = "version")
public class Version {
    private int id;
    private Project project;
    private String version;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
