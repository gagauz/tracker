package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.CollectionType;
import com.gagauz.tracker.db.base.Identifiable;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.util.Collection;
import java.util.EnumSet;

@Entity
@Table(name = "role_group")
@TypeDefs({
        @TypeDef(name = "setOf.Enum",
                typeClass = CollectionType.class,
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

    @JoinColumn
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

    public void setRoles(Collection<Role> roles) {
        if (null != roles && !(roles instanceof EnumSet)) {
            roles = EnumSet.copyOf(roles);
        }
        this.roles = (EnumSet<Role>) roles;
    }

    @Override
    public int hashCode() {
        return (null == project ? 0 : project.getId()) + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

}
