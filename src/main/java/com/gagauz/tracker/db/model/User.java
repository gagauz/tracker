package com.gagauz.tracker.db.model;

import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.db.base.Identifiable;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "`user`")
public class User implements Identifiable, Serializable, SecurityUser {
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
    @SequenceGenerator(name = "id_sequence", sequenceName = "user_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_sequence")
    @Column(unique = true, nullable = false)
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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (null != obj && obj.hashCode() == hashCode());
    }

    @Override
    public String toString() {
        return "User <id=" + id + ">";
    }
}
