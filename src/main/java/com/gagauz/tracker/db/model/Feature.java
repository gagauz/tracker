package com.gagauz.tracker.db.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "feature")
public class Feature extends TimeTrackedEntity {
	private Project project;
	private User creator;
	private List<FeatureVersion> featureVersions;
	private String name;
	private String description;

	public Feature() {

	}

	public Feature(int id) {
		setId(id);
	}

	@ForeignKey(name = "fk_feature_project")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ForeignKey(name = "fk_feature_owner")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id.featureId")
	public List<FeatureVersion> getFeatureVersions() {
		return this.featureVersions;
	}

	public void setFeatureVersions(List<FeatureVersion> featureVersions) {
		this.featureVersions = featureVersions;
	}

	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	@Lob
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Feature<id=" + getId() + ">";
	}
}
