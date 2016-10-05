package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class CvsRepo {
	private CvsType type;
	private String url;
	private String username;
	private String password;
	private String branch;
	private String repoPath;

	@Column
	@Enumerated(EnumType.STRING)
	public CvsType getType() {
		return this.type;
	}

	public void setType(CvsType type) {
		this.type = type;
	}

	@Column
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column
	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Column
	public String getRepoPath() {
		return this.repoPath;
	}

	public void setRepoPath(String repoPath) {
		this.repoPath = repoPath;
	}

}
