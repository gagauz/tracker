package com.gagauz.tracker.db.model;

import java.util.Collection;
import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.gagauz.hibernate.types.CollectionType;
import org.gagauz.hibernate.types.HashSetType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;


@Entity
@Table(name = "role_group")
@TypeDefs({
    @TypeDef(name = "setOf.String", typeClass = HashSetType.class, parameters = {
            @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.AccessRole"),
            @Parameter(name = CollectionType.SERIALIZER, value = "org.gagauz.hibernate.model.base.EnumSerializer")
    })
})
public class RoleGroup extends BaseEntity {
    private String name;
    private Project project;
    private EnumSet<AccessRole> roles = EnumSet.noneOf(AccessRole.class);

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
        this.roles = null == roles || roles.isEmpty() ? EnumSet.noneOf(AccessRole.class) : EnumSet.copyOf(roles);
    }
}
