package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "feature_version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"feature_id", "version_id"})
})
public class FeatureVersion implements Identifiable, Serializable {

    private static final long serialVersionUID = -8693198398126115278L;
    private int id;
    private Feature feature;
    private Version version;
    private User creator;
    private User owner;
    private Date created = new Date();
    private Date updated = new Date();
    private String description;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ForeignKey(name = "fk_featureVersion_feature")
    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @ForeignKey(name = "fk_featureVersion_version")
    @JoinColumn(name = "version_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @ForeignKey(name = "fk_featureVersion_creator")
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ForeignKey(name = "fk_featureVersion_owner")
    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    //    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureVersion")
    //    public List<Ticket> getTickets() {
    //        return tickets;
    //    }
    //
    //    public void setTickets(List<Ticket> tickets) {
    //        this.tickets = tickets;
    //    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj) || (obj != null && (((FeatureVersion) obj).id == id));
    }

    @Override
    public int hashCode() {
        return id;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
