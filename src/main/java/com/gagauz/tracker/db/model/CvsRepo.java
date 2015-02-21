package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CvsRepo {
    private String url;
    private String username;
    private String password;
    private String branch;

    @Column
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column
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

    @Column
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
