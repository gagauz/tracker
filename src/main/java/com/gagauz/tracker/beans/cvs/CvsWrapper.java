package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;

import java.util.List;

public interface CvsWrapper {
    void init(Project project);

    List<Commit> getCommits();

    List<Commit> getCommits(Ticket ticket);

}
