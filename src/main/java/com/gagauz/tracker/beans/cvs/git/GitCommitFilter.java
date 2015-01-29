package com.gagauz.tracker.beans.cvs.git;

import com.gagauz.tracker.beans.cvs.CommitFilter;

public class GitCommitFilter implements CommitFilter {
    private String grep;

    public String getGrep() {
        return grep;
    }

    public void setGrep(String grep) {
        this.grep = grep;
    }

}
