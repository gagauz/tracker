package com.gagauz.tracker.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.gagauz.tracker.db.model.FeatureVersion.FeatureVersionId;
import com.xl0e.hibernate.model.IModel;

@Entity
@Table(name = "feature_version")
public class FeatureVersion implements IModel<FeatureVersionId>, Serializable {

    @Embeddable
    public static class FeatureVersionId implements Serializable {
        private static final long serialVersionUID = 1939148011273312467L;
        private int projectId;
        private int featureId;
        private int versionId;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getFeatureId() {
            return featureId;
        }

        public void setFeatureId(int featureId) {
            this.featureId = featureId;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj || this == obj || !(obj instanceof FeatureVersionId)) {
                return this == obj;
            }
            FeatureVersionId other = (FeatureVersionId) obj;
            return other.getProjectId() == projectId && other.getVersionId() == versionId && other.getVersionId() == versionId;
        }

        @Override
        public String toString() {
            return String.valueOf(featureId) + '_' + versionId;
        }

    }

    private static final long serialVersionUID = -8693198398126115278L;
    private FeatureVersionId id;
    private User creator;
    private Project project;
    private Feature feature;
    private Version version;

    @Override
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", insertable = false, updatable = false, referencedColumnName = "id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        getId().projectId = null != project ? project.getId() : 0;
    }

    @ForeignKey(name = "fk_featureVersion_feature")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featureId", insertable = false, updatable = false, referencedColumnName = "id")
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
        getId().projectId = null != feature && null != feature.getProject() ? feature.getProject().getId() : 0;
        getId().featureId = null != feature ? feature.getId() : 0;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "versionId", insertable = false, updatable = false, referencedColumnName = "id")
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
        getId().projectId = null != version && null != version.getProject() ? version.getProject().getId() : 0;
        getId().versionId = null != version ? version.getId() : 0;
    }

    @Override
    public String toString() {
        return "FeatureVersion <feature=" + getId().getFeatureId() + ", version=" + getId().getVersionId() + ">";
    }

    @Override
    public int hashCode() {
        return getId().getProjectId() ^ (getId().getFeatureId() >>> 32) ^ (getId().getVersionId() >>> 32);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (obj instanceof FeatureVersion) {
            return getId().equals(((FeatureVersion) obj).getId());
        }
        return false;
    }
}
