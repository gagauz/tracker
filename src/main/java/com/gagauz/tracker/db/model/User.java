package com.gagauz.tracker.db.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gagauz.utils.CryptoUtils;

import com.gagauz.tracker.db.base.Identifiable;
import com.gagauz.tracker.utils.HashUtils;

@Entity
@Table(name = "`user`")
public class User implements Identifiable, Serializable, org.gagauz.tapestry.security.api.User {
    private static final long serialVersionUID = 7903294228565311630L;
    private int id;
    private String name;
    private String email;
    private Collection<RoleGroup> roleGroups = new HashSet<RoleGroup>();
    private String username;
    private String password;
    private String token;
    private Set<Roles> roles;

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

    @Column(nullable = false)
    public String getToken() {
        if (null == token) {
            token = HashUtils.md5(getEmail() + getPassword());
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @Transient
    public boolean checkRoles(Roles[] rolesToCheck) {
        if (null == roles) {
            Set<Roles> roleSet = new HashSet<>();
            for (RoleGroup group : roleGroups) {
                roleSet.addAll(group.getRoles());
            }
            roles = roleSet;
        }
        if (null == rolesToCheck || rolesToCheck.length == 0) {
            return true;
        }
        for (Roles role : rolesToCheck) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public boolean checkPassword(String password) {
        return this.password.equals(password)
                || this.password.equals(CryptoUtils.createSHA512String(password));
    }
}
