package com.gagauz.tracker.db.model;

import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.util.List;

@Entity
@DiscriminatorFormula("featureVersion_id is null")
@DiscriminatorValue("false")
public class Ticket extends Bug {
    private FeatureVersion featureVersion;
    private List<Bug> bugs;

    @ForeignKey(name = "fk_ticket_featureVersion")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public FeatureVersion getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(FeatureVersion featureVersion) {
        this.featureVersion = featureVersion;
    }

    @Transient
    public Feature getFeature() {
        return featureVersion.getFeature();
    }

    @Transient
    public Version getVersion() {
        return featureVersion.getVersion();
    }

    @Override
    @Transient
    public String getType() {
        return "TASK";
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }
}
