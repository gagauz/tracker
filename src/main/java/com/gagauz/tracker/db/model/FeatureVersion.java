package com.gagauz.tracker.db.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
        private int featureId;
        private int versionId;

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
            return other.getFeatureId() == featureId && other.getVersionId() == versionId;
        }

        @Override
        public String toString() {
            return String.valueOf(featureId) + '_' + versionId;
        }

    }

    private static final long serialVersionUID = -8693198398126115278L;
    private FeatureVersionId id;
    private User creator;
    private Feature feature;
    private Version version;

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
        return getFeature().getProject();
    }

    @ForeignKey(name = "fk_featureVersion_feature")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featureId", insertable = false, updatable = false, referencedColumnName = "id")
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
        getId().featureId = null != feature ? feature.getId() : 0;
    }

    @ForeignKey(name = "fk_featureVersion_version")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "versionId", insertable = false, updatable = false, referencedColumnName = "id")
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
        getId().versionId = null != version ? version.getId() : 0;
    }

    @Override
    public String toString() {
        return "FeatureVersion <feature=" + getId().getFeatureId() + ", version=" + getId().getVersionId() + ">";
    }

    @Override
    public int hashCode() {
        return getId().getFeatureId() ^ (getId().getVersionId() >>> 32);
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
