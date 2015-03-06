package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.HashSetType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "role_group")
@TypeDefs({
        @TypeDef(name = "setOf.String",
                typeClass = HashSetType.class,
                parameters = {
                        @Parameter(name = CollectionType.CLASS, value = "java.lang.String"),
                        @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.StringSerializer")
                }
        )
})
public class RoleGroup implements Identifiable {
    private int id;
    private String name;
    private Project project;
    private Collection<String> roles = new HashSet<String>();

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

    @ForeignKey(name = "fk_roleGroup_project")
    @JoinColumn(nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(nullable = false)
    @Type(type = "setOf.String")
    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

}
