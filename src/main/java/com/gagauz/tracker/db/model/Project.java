package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;

@Entity
@Table(name = DB.Table.project)
public class Project extends Model {
	private static final long serialVersionUID = 4883263241972298960L;
	private String code;
	private String name;
	private CvsRepo cvsRepo;
	private List<Version> versions;
	private List<Feature> features;
	private String currentCvsVersion;

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

	@Embedded
	public CvsRepo getCvsRepo() {
		// if (null == cvsRepo) {
		// cvsRepo = new CvsRepo();
		// }
		// if (null == cvsRepo.getRepoPath()) {
		// cvsRepo.setRepoPath(PathUtils.getProjectBaseDir(this));
		// }
		return this.cvsRepo;
	}

	public void setCvsRepo(CvsRepo cvsRepo) {
		this.cvsRepo = cvsRepo;
	}

	@Column
	public String getCurrentCvsVersion() {
		return this.currentCvsVersion;
	}

	public void setCurrentCvsVersion(String currentCvsVersion) {
		this.currentCvsVersion = currentCvsVersion;
	}

}
