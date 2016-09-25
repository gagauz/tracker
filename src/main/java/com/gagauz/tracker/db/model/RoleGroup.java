package com.gagauz.tracker.db.model;

import java.util.Collection;
import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.HashSetType;
import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "role_group")
@TypeDefs({
    @TypeDef(name = "setOf.String", typeClass = HashSetType.class, parameters = {
            @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.AccessRole"),
            @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.EnumSerializer")
    })
})
public class RoleGroup implements Identifiable {
    private int id;
    private String name;
    private Project project;
    private EnumSet<AccessRole> roles = EnumSet.noneOf(AccessRole.class);

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
    public Collection<AccessRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<AccessRole> roles) {
        this.roles = null == roles ? EnumSet.noneOf(AccessRole.class) : EnumSet.copyOf(roles);
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
