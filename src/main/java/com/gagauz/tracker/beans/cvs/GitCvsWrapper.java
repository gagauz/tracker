package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.beans.cvs.git.GitCommitFilter;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.utils.BashUtils;
import com.gagauz.tracker.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GitCvsWrapper implements CvsWrapper {

    private File repoDir;

    @Override
    public void init(String repo) {

        String str = BashUtils.execute("git", "--version");
        if (!str.contains("git version")) {
            throw new RuntimeException("Failed to init Git wrapper. Chek if git executable exits and available via system Path variable.");
        }

        this.repoDir = new File(repo);
    }

    private String log(String grep) {
        String log = BashUtils.execute(repoDir, "git", "log", "--pretty=format:>>>%at|%H|%an|%s", "--stat", "--name-status", "--grep=" + grep,
                "--regexp-ignore-case");
        return log;
    }

    private String log() {
        String log = BashUtils.execute(repoDir, "git", "log", "--pretty=format:>>>%at|%H|%an|%s", "--stat", "--name-status");
        return log;
    }

    @Override
    public List<Commit> getCommits() {
        String log = log();
        List<Commit> commits = new ArrayList<Commit>();
        for (String cl : StringUtils.split(log, ">>>")) {
            if (!"".equals(cl)) {
                String[] lines = StringUtils.split(cl, "\n", 2);
                String[] fields = StringUtils.split(lines[0], "|");
                Commit commit = new Commit();
                commit.setHash(fields[1]);
                commit.setDate(new Date(Long.parseLong(fields[0])));
                commit.setAuthor(fields[2]);
                commit.setComment(fields[3]);
                commit.setDetails(lines[1]);
                commits.add(commit);
            }
        }
        return commits;
    }

    @Override
    public List<Commit> getCommits(CommitFilter filter) {
        GitCommitFilter gitFilter = (GitCommitFilter) filter;
        String log = log(gitFilter.getGrep());
        List<Commit> commits = new ArrayList<Commit>();
        for (String cl : StringUtils.split(log, ">>>")) {
            if (!"".equals(cl)) {
                String[] lines = StringUtils.split(cl, "\n", 2);
                String[] fields = StringUtils.split(lines[0], "|");
                Commit commit = new Commit();
                commit.setHash(fields[1]);
                commit.setDate(new Date(Long.parseLong(fields[0]) * 1000));
                commit.setAuthor(fields[2]);
                commit.setComment(fields[3]);
                commit.setDetails(lines[1]);
                commits.add(commit);
            }
        }
        return commits;
    }

    public static void main(String[] args) {
        GitCvsWrapper w = new GitCvsWrapper();
        w.init("R:\\my-projects\\tracker\\");
        w.log("Task");
    }
}
