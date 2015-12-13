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
public class FeatureVersion {

    @Embeddable
    public static class Id implements Serializable {
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

    }

    private static final long serialVersionUID = -8693198398126115278L;
    private Id id;
    private User creator;

    @EmbeddedId
    public Id getId() {
        if (null == id) {
            id = new Id();
        }
        return id;
    }

    public void setId(Id id) {
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
}
