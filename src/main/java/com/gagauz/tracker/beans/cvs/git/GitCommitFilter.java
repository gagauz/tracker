package com.gagauz.tracker.beans.cvs.git;

import com.gagauz.tracker.beans.cvs.CvsCommitFilter;

public class GitCommitFilter implements CvsCommitFilter {
    private String grep;

    public String getGrep() {
        return grep;
    }

    public void setGrep(String grep) {
        this.grep = grep;
    }

}
