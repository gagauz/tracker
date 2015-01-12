package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

    private int id;
    private String key1;
    private String name;
    private String cvsRepositoryPath;
    private List<Version> versions;
    private List<Feature> features;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Column
    public String getCvsRepositoryPath() {
        return cvsRepositoryPath;
    }

    public void setCvsRepositoryPath(String cvsRepositoryPath) {
        this.cvsRepositoryPath = cvsRepositoryPath;
    }

}
