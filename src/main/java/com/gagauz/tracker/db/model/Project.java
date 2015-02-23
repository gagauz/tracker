package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;
import com.gagauz.tracker.utils.PathUtils;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

    private int id;
    private String key1;
    private String name;
    private CvsRepo cvsRepo;
    private List<Version> versions;
    private List<Feature> features;
    private String currentCvsVersion;

    @Override
    @Id
    @SequenceGenerator(name = "id_sequence", sequenceName = "project_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_sequence")
    @Column(unique = true, nullable = false)
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

    @Embedded
    public CvsRepo getCvsRepo() {
        if (null == cvsRepo) {
            cvsRepo = new CvsRepo();
        }
        if (null == cvsRepo.getRepoPath()) {
            cvsRepo.setRepoPath(PathUtils.getProjectBaseDir(this));
        }
        return cvsRepo;
    }

    public void setCvsRepo(CvsRepo cvsRepo) {
        this.cvsRepo = cvsRepo;
    }

    @Column
    public String getCurrentCvsVersion() {
        return currentCvsVersion;
    }

    public void setCurrentCvsVersion(String currentCvsVersion) {
        this.currentCvsVersion = currentCvsVersion;
    }

}
