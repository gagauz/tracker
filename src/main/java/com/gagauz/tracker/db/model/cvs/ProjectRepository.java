package com.gagauz.tracker.db.model.cvs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;
import com.gagauz.tracker.db.model.Project;

@Entity
@Table(name = DB.Table.Cvs.project_repository, uniqueConstraints = @UniqueConstraint(columnNames = { DB.Column.project_id, DB.Column.repository_id, DB.Column.name }))
public class ProjectRepository extends Model {
    /**
     * Project ref
     */
    private Project project;
    /**
     * Project name in CVS repository
     */
    private String name;
    /**
     * CVS repository ref
     */
    private Repository repository;

    @OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = DB.Column.project_id)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = true, name = DB.Column.name)
    public String getName() {
        return name;
    }

    public void setName(String cvsName) {
        this.name = cvsName;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = DB.Column.repository_id)
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

}
