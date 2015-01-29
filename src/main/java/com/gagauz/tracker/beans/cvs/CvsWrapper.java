package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.db.model.Commit;

import java.util.List;

public interface CvsWrapper {
    void init(String repo);

    List<Commit> getCommits();

    List<Commit> getCommits(CommitFilter filter);
}
