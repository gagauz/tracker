package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Project;

import java.util.List;

public interface CvsWrapper {
    void init(Project repo);

    List<Commit> getCommits();

    List<Commit> getCommits(CommitFilter filter);
}
