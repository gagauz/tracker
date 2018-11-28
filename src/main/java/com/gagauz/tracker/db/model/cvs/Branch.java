package com.gagauz.tracker.db.model.cvs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;

@Entity
@Table(name = DB.Table.Cvs.branch)
public class Branch extends Model {
    private String name;
    private Version version;
    private Ticket ticket;
    private Commit lastCommit;

    @Transient
    public ProjectRepository getRepository() {
        if (null != version) {
            return version.getProject().getProjectRepository();
        }
        if (null != ticket) {
            return ticket.getProject().getProjectRepository();
        }
        return null;
    }

    public void setRepository(ProjectRepository repository) {
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    public Commit getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(Commit lastCommit) {
        this.lastCommit = lastCommit;
    }
}
