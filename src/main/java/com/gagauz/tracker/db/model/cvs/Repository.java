package com.gagauz.tracker.db.model.cvs;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;

@Entity
@Table(name = DB.Table.Cvs.repository)
public class Repository extends Model {
    private Provider provider;
    private String username;
    private String password;
    private String name;
    private Oauth oauth;

    @Enumerated(EnumType.STRING)
    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Embedded
    public Oauth getOauth() {
        if (null == oauth) {
            oauth = new Oauth();
        }
        return oauth;
    }

    public void setOauth(Oauth oauth) {
        this.oauth = oauth;
    }

}
