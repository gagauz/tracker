package com.gagauz.tracker.db.model;

import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "user")
public class User implements Identifiable {
    private int id;
    private String name;
    private String email;
    private Collection<RoleGroup> roleGroups = new HashSet<RoleGroup>();
    private String password;

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
        this.roleGroups = roleGroups;
    }
}
