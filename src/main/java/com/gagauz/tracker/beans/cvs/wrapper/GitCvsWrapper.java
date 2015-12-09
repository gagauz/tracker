package com.gagauz.tracker.beans.cvs.wrapper;

import com.gagauz.tracker.beans.cvs.CvsWrapper;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.utils.BashUtils;
import com.gagauz.tracker.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GitCvsWrapper implements CvsWrapper {

    private File repoDir;

    @Override
    public void init(Project project) {

        String str = BashUtils.execute("git --version");
        if (!str.contains("git version")) {
            throw new RuntimeException("Failed to init Git wrapper. Chek if git executable exits and available via system Path variable.");
        }

        File repoDir = new File(project.getCvsRepo().getRepoPath());
        if (!repoDir.exists() || !repoDir.isDirectory()) {
            System.err.println("Directory doesn't exists, create new.");
            repoDir.mkdirs();
        }

        if (!checkIfIsRepo(repoDir)) {
            System.err.println("Directory is not repo, clone into directory.");
            cloneGitRepo(repoDir, project);
        }

        this.repoDir = repoDir;
    }

    private void cloneGitRepo(File directory, Project project) {
        String url = project.getCvsRepo().getUrl();
        if (project.getCvsRepo().getUsername() != null) {
            String cred = project.getCvsRepo().getUsername();
            if (project.getCvsRepo().getPassword() != null) {
                cred += ':' + project.getCvsRepo().getPassword();
            }
            cred += '@';

            url = url.replace("://", "://" + cred);
        }
        String out = BashUtils.execute(directory.getParentFile(), "git clone %s %s", url, directory.getName());
        System.out.println(out);
    }

    private boolean checkIfIsRepo(File directory) {
        String res = BashUtils.execute(directory, "git rev-parse --is-inside-work-tree");
        System.out.println(res);
        return "true".equals(res);
    }

    private String log(String grep) {
        StringBuilder log = new StringBuilder();
        int res = BashUtils.execute(repoDir, log, "git log --pretty=format:>>>%at|%H|%an|%s --stat --name-status --grep=" + grep +
                " --regexp-ignore-case");
        if (0 != res) {
            System.err.println(log.toString());
            return "";
        }
        return log.toString();
    }

    private String log() {
        String log = BashUtils.execute(repoDir, "git log --pretty=format:'>>>%at|%H|%an <%ae>|%s' --stat --name-status");
        return log;
    }

    @Override
    public List<Commit> getCommits() {
        String log = log();
        List<Commit> commits =new ArrayList<>();
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
    public List<Commit> getCommits(Ticket ticket) {
        String grep = ticket.getType() + "\\s#" + ticket.getId();
        String log = log(grep);
        List<Commit> commits =new ArrayList<>();
        for (String cl : log.split(">>>")) {
            if (!"".equals(cl)) {
                String[] lines = StringUtils.split(cl, "\n", 2);
                String[] fields = StringUtils.split(lines[0], "|");
                if (fields.length > 1) {
                    Commit commit = new Commit();
                    commit.setHash(fields[1]);
                    commit.setDate(new Date(Long.parseLong(fields[0]) * 1000));
                    commit.setAuthor(fields[2]);
                    commit.setComment(fields[3]);
                    commit.setDetails(lines[1]);
                    commits.add(commit);
                }
            }
        }
        return commits;
    }
}
