package com.gagauz.tracker.db.model;

import java.util.List;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

@Entity
@Table(name = "project")
public class Project implements Identifiable {

    private int id;
    private String name;
	private List<Version> versions;

 

	@Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="project")
	public List<Version> getVersions() {
		return versions;
	}
    
    public void setVersions(List<Version> versions) {
 		this.versions = versions;
 	}
}
