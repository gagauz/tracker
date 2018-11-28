package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;
import com.gagauz.tracker.db.model.cvs.ProjectRepository;

@Entity
@Table(name = DB.Table.project)
public class Project extends Model {
    private static final long serialVersionUID = 4883263241972298960L;
    /**
     * Project code for features and tickets prefix
     */
    private String code;
    /**
     * Displayed project name
     */
    private String name;

    /**
     * All project versions
     */
    private List<Version> versions;
    /**
     * All project features
     */
    private List<Feature> features;

    private ProjectRepository projectRepository;

    @Column(nullable = false, unique = true)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    public List<Version> getVersions() {
        return this.versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    public List<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, mappedBy = "project")
    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

}
