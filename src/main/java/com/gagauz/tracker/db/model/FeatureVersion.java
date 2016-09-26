package com.gagauz.tracker.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "feature_version")
public class FeatureVersion implements Serializable {

	@Embeddable
	public static class FeatureVersionId implements Serializable {
		private static final long serialVersionUID = 1939148011273312467L;
		private Feature feature;
		private Version version;

		@ForeignKey(name = "fk_featureVersion_feature")
		@ManyToOne(fetch = FetchType.LAZY)
		public Feature getFeature() {
			return feature;
		}

		public void setFeature(Feature feature) {
			this.feature = feature;
		}

		@ForeignKey(name = "fk_featureVersion_version")
		@ManyToOne(fetch = FetchType.LAZY)
		public Version getVersion() {
			return version;
		}

		public void setVersion(Version version) {
			this.version = version;
		}

		@Override
		public boolean equals(Object obj) {
			if (null == obj || this == obj || !(obj instanceof FeatureVersionId)) {
				return this == obj;
			}
			FeatureVersionId other = (FeatureVersionId) obj;
			return other.getFeature().equals(feature) && other.getVersion().equals(version);
		}

	}

	private static final long serialVersionUID = -8693198398126115278L;
	private FeatureVersionId id;
	private User creator;

	@EmbeddedId
	public FeatureVersionId getId() {
		if (null == id) {
			id = new FeatureVersionId();
		}
		return id;
	}

	public void setId(FeatureVersionId id) {
		this.id = id;
	}

	@ForeignKey(name = "fk_featureVersion_creator")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Transient
	public Project getProject() {
		return getId().getFeature().getProject();
	}

	@Transient
	public Version getVersion() {
		return getId().getVersion();
	}

	@Transient
	public Feature getFeature() {
		return getId().getFeature();
	}

	@Override
	public String toString() {
		return "FeatureVersion <feature=" + id.getFeature() + ", version=" + id.getVersion() + ">";
	}
}
