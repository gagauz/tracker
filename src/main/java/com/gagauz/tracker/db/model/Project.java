package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

	private int id;
	private String code;
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
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (null != obj && obj.hashCode() == hashCode());
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
