package com.gagauz.tracker.db.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.xl0e.hibernate.model.Model;
import com.xl0e.hibernate.types.CollectionType;
import com.xl0e.hibernate.types.HashSetType;
import com.xl0e.util.C;

@Entity
@Table(name = "role_group")
@TypeDefs({
        @TypeDef(name = "setOf.String", typeClass = HashSetType.class, parameters = {
                @Parameter(name = CollectionType.CLASS, value = "java.lang.String"),
                @Parameter(name = CollectionType.SERIALIZER, value = "com.xl0e.hibernate.model.base.StringSerializer")
        })
})
public class RoleGroup extends Model {
    private static final long serialVersionUID = 5710346787058144797L;
    private String name;
    private Project project;
    private Set<String> roles = Collections.emptySet();

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = null == roles || roles.isEmpty() ? Collections.emptySet() : C.hashSet(roles);
    }
}
