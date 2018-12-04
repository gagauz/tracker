package com.gagauz.tracker.db.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.tapestry5.security.api.AccessAttributes;
import org.apache.tapestry5.web.services.security.SecuredAccessAttributes;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;
import com.gagauz.tracker.utils.HashUtils;
import com.xl0e.util.C;
import com.xl0e.util.CryptoUtils;

@Entity
@Table(name = "`user`")
public class User extends Model implements org.apache.tapestry5.security.api.User {
    private static final long serialVersionUID = 7903294228565311630L;
    private String name;
    private String email;
    private String username;
    private String password;
    private String token;
    private Set<UserGroup> userGroups;
    private transient AccessAttributes accessAttributes;

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

    @Transient
    public void setPasswordRaw(String password) {
        setPassword(CryptoUtils.createSHA512String(password));
    }

    @JoinTable(name = DB.Table.user_to_user_groups)
    @ManyToMany(fetch = FetchType.LAZY)
    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroup> userGroups) {
        this.userGroups = userGroups;
        this.accessAttributes = null;
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
    public String toString() {
        return "User <id=" + getId() + ">";
    }

    @Transient
    @Override
    public AccessAttributes getAccessAttributes() {
        if (null == accessAttributes) {
            Set<String> roles = C.hashSet();
            for (UserGroup group : userGroups) {
                roles.addAll(C.emptyIfNull(group.getRoles()));
            }
            accessAttributes = new SecuredAccessAttributes(roles);
        }
        return accessAttributes;
    }
}
