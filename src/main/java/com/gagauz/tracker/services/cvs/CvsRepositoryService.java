package com.gagauz.tracker.services.cvs;

import java.util.List;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;

public interface CvsRepositoryService {

    boolean hasBranches(Version version);

    List<Branch> getBranches(Version version);

    void createBranch(Branch branch);

    default Branch getDefaultBranch(Version version) {
        Branch branch = new Branch();
        branch.setVersion(version);
        branch.setName("version/" + version.getName().toLowerCase());
        return branch;
    }

    void deleteBranch(Branch branch);

    void connectVersionWithVCS(List<Version> versions);

}
