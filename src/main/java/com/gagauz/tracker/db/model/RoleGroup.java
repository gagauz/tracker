package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.EnumSetType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.EnumSet;

@Entity
@Table(name = "role_group")
@TypeDefs({
        @TypeDef(name = "setOf.Enum",
                typeClass = EnumSetType.class,
                parameters = {
                        @Parameter(name = CollectionType.CLASS, value = "com.gagauz.tracker.db.model.Role"),
                        @Parameter(name = CollectionType.SERIALIZER, value = "com.gagauz.tracker.db.utils.RoleSerializer")
                }
        )
})
public class RoleGroup implements Identifiable {
    private int id;
    private String name;
    private Project project;
    private EnumSet<Role> roles = EnumSet.noneOf(Role.class);

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
    @Type(type = "setOf.Enum")
    public EnumSet<Role> getRoles() {
        return roles;
    }

    public void setRoles(EnumSet<Role> roles) {
        if (null != roles && !(roles instanceof EnumSet)) {
            roles = EnumSet.copyOf(roles);
        }
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
