package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "version"})
})
public class Version implements Identifiable {

    private int id;
    private Project project;
    private Date created = new Date();
    private Date updated = new Date();
    private String version;
    private List<Task> tasks;
    private Date releaseDate = new Date();
    private boolean released = false;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumn(nullable = false)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "version")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column
    @Temporal(TemporalType.DATE)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column
    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

}
