package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

    private int id;
    private String key;
    private String name;
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
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

}
