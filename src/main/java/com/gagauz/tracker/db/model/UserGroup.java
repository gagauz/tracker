package com.gagauz.tracker.db.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gagauz.tracker.db.base.DB;
import com.xl0e.hibernate.types.CollectionType;
import com.xl0e.hibernate.types.HashSetType;

@Entity
@Table(name = DB.Table.user_group, uniqueConstraints = {
		@UniqueConstraint(columnNames = { DB.Column.project_id, "name" })
})
@TypeDefs({
		@TypeDef(name = "setOf.String", typeClass = HashSetType.class, parameters = {
				@Parameter(name = CollectionType.CLASS, value = "java.lang.String"),
				@Parameter(name = CollectionType.SERIALIZER, value = "com.xl0e.hibernate.model.base.StringSerializer")
		})
})
public class UserGroup extends TimeTrackedEntity {
	private static final long serialVersionUID = 5710346787058144797L;
	private String name;
	private Project project;
	private Set<String> roles;

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = DB.Column.project_id)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column
	@Type(type = "setOf.String")
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
