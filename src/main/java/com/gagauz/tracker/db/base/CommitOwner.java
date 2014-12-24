package com.gagauz.tracker.db.base;

import com.gagauz.tracker.db.model.Commit;

import javax.persistence.OneToMany;

import java.util.List;

public abstract class CommitOwner {
    private List<Commit> commits;

    @OneToMany
    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
