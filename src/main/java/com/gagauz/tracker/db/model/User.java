package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gagauz.tracker.db.base.Identifiable;
import com.gagauz.tracker.web.services.security.SessionUser;

@Entity
@Table(name = "user")
public class User implements Identifiable, Serializable, SessionUser {
    private static final long serialVersionUID = 7903294228565311630L;
    private int id;
    private String name;
    private String email;
    private Collection<RoleGroup> roleGroups = new HashSet<RoleGroup>();
    private String username;
    private String password;
    private Role[] roles;

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

    @Column(nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JoinTable(name = "user_roles")
    @ManyToMany(fetch = FetchType.LAZY)
    public Collection<RoleGroup> getRoleGroups() {
        return roleGroups;
    }

    public void setRoleGroups(Collection<RoleGroup> roleGroups) {
        this.roles = null;
        this.roleGroups = roleGroups;
    }

    @Override
    @Transient
    public Role[] getRoles() {
        if (null == roles) {
            HashSet<Role> roleSet = new HashSet<Role>();
            for (RoleGroup group : roleGroups) {
                roleSet.addAll(group.getRoles());
            }
            roles = roleSet.toArray(new Role[roleSet.size()]);
        }
        return roles;
    }

}
