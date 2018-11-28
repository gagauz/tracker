package com.gagauz.tracker.web.pages.version;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.web.services.annotation.PageContext;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;
import com.gagauz.tracker.services.cvs.CvsRepositoryService;
import com.gagauz.tracker.web.pages.Index;

public class VersionRepository {
    @PageContext(index = 0)
    @Property(write = false)
    protected Version version;

    @Property
    private Branch newBranch;

    @Property
    private Branch branch;

    @Inject
    private CvsRepositoryService cvsRepositoryService;

    public Object onActivate() {
        if (null == version) {
            return Index.class;
        }
        if (null == newBranch) {
            newBranch = cvsRepositoryService.getDefaultBranch(version);
        }
        return null;
    }

    @Cached
    public List<Branch> getBranches() {
        return cvsRepositoryService.getBranches(version);
    }

    public void onSuccessFromCreateBranch() {
        cvsRepositoryService.createBranch(newBranch);
    }

    public void onDeleteBranch(Branch branch) {
        cvsRepositoryService.deleteBranch(branch);
    }
}
