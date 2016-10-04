package com.gagauz.tracker.db.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "version", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "project_id", "name" })
})
public class Version extends TimeTrackedEntity implements Comparable<Version> {

	private Project project;
	private String name;
	private String cvsBranchName;
	private List<FeatureVersion> featureVersion;
	private Date releaseDate = new Date();
	private boolean released = false;

	public Version() {
		// default constructor
	}

	@ForeignKey(name = "fk_version_project")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
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
	public String getCvsBranchName() {
		if (null == this.cvsBranchName) {
			this.cvsBranchName = getProject().getCode() + '-' + getName();
		}
		return this.cvsBranchName;
	}

	public void setCvsBranchName(String branch) {
		this.cvsBranchName = branch;
	}

	@ForeignKey(name = "fk_version_features")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id.versionId")
	public List<FeatureVersion> getFeatureVersions() {
		return this.featureVersion;
	}

	public void setFeatureVersions(List<FeatureVersion> featureVersion) {
		this.featureVersion = featureVersion;
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
	public String toString() {
		return "Version<id=" + getId() + ">";
	}

	@Override
	public int compareTo(Version o) {
		return getName().compareTo(o.getName());
	}
}
