package com.gagauz.tracker.beans.cvs.wrapper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gagauz.tracker.beans.cvs.CvsWrapper;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.utils.BashUtils;
import com.gagauz.tracker.utils.StringUtils;

public class SvnCvsWrapper implements CvsWrapper {

    private File repoDir;

    @Override
    public void init(Project project) {

        String repoPath = project.getCvsRepo().getRepoPath();

        File repoDir = new File(repoPath);

        if (!repoDir.exists() || !repoDir.isDirectory()) {
            System.err.println("Directory doesn't exists, create new.");
            repoDir.mkdirs();
        }

        String str = BashUtils.execute(repoDir, "svn --version");
        if (!str.contains("svn, version")) {
            throw new RuntimeException("Failed to init Svn wrapper. Chek if svn executable exits and available via system Path variable.");
        }

        if (!checkIfIsRepo(repoDir)) {
            System.err.println("Directory is not repo, checkout into directory.");
            checkoutSvnRepo(repoDir, project);
        }

        if (project.getCvsRepo().getBranch() != null) {
            final String branch = project.getCvsRepo().getBranch();
            File[] files = repoDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File arg0) {
                    return arg0.getName().equals(branch);
                }
            });
            if (files.length == 1) {
                repoDir = files[0];
                return;
            } else {
                throw new RuntimeException("Unable to find directory " + branch + " in repo!");
            }
        }

        this.repoDir = repoDir;
    }

    private void checkoutSvnRepo(File directory, Project project) {
        String url = project.getCvsRepo().getUrl();
        if (project.getCvsRepo().getUsername() != null) {
            url += " --username " + project.getCvsRepo().getUsername();
            if (project.getCvsRepo().getPassword() != null) {
                url += " --password " + project.getCvsRepo().getPassword();
            }
        }
        String out = BashUtils.execute(directory.getParentFile(), "svn checkout %s %s", url, directory.getName());
        System.out.println(out);
    }

    private boolean checkIfIsRepo(File directory) {
        String res = BashUtils.execute(directory, "svn info");
        System.out.println(res);
        return !res.contains("is not a working copy");
    }

    private String log(String grep) {
        StringBuilder log = new StringBuilder();
        // Stupid SVN doesn't event have case insensitive search
        int res = BashUtils.execute(repoDir, log, "svn log --verbose --search \"" + grep + '"');
        if (0 != res) {
            System.err.println(log.toString());
            return "";
        }
        return log.toString();
    }

    private String log() {
        String log = BashUtils.execute(repoDir, "svn log --verbose");
        return log;
    }

    @Override
    public List<Commit> getCommits() {
        String log = log();
        List<Commit> commits = new ArrayList<>();
        for (String cl : StringUtils.split(log, "------------------------------------------------------------------------")) {
            if (!"".equals(cl.trim())) {
                String[] lines = StringUtils.split(cl, "\n");
                String[] fields = StringUtils.split(lines[0], "|");
                Commit commit = new Commit();
                commit.setId(fields[0].trim());
                commit.setAuthor(fields[1].trim());
                commit.setDate(new Date(fields[2].trim()));
                int numOfComments = Integer.parseInt(fields[3].replaceAll("[^0-9]", ""));
                StringBuilder com = new StringBuilder();
                StringBuilder det = new StringBuilder();
                if (lines.length - 3 > numOfComments) {
                    for (int i = 2; i < lines.length - numOfComments - 1; i++) {
                        det.append(lines[i].trim());
                    }
                    for (int i = lines.length - numOfComments; i < lines.length; i++) {
                        com.append(lines[i].trim());
                    }
                }
                commit.setComment(com.toString());
                commit.setDetails(det.toString());
                commits.add(commit);
            }
        }
        return commits;
    }

    @Override
    public List<Commit> getCommits(Ticket ticket) {
        String grep = ticket.getFeature().getProject().getCode() + " #" + ticket.getId();
        String log = log(grep);
        List<Commit> commits = new ArrayList<>();
        for (String cl : StringUtils.split(log, "------------------------------------------------------------------------")) {
            if (!"".equals(cl.trim())) {
                String[] lines = StringUtils.split(cl, "\n");
                String[] fields = StringUtils.split(lines[0], "|");
                Commit commit = new Commit();
                commit.setId(fields[0].trim());
                commit.setAuthor(fields[1].trim());
                commit.setDate(new Date(fields[2].trim()));
                int numOfComments = Integer.parseInt(fields[3].replaceAll("[^0-9]", ""));
                StringBuilder com = new StringBuilder();
                StringBuilder det = new StringBuilder();
                if (lines.length - 3 > numOfComments) {
                    for (int i = 2; i < lines.length - numOfComments - 1; i++) {
                        det.append(lines[i].trim());
                    }
                    for (int i = lines.length - numOfComments; i < lines.length; i++) {
                        com.append(lines[i].trim());
                    }
                }
                commit.setComment(com.toString());
                commit.setDetails(det.toString());
                commits.add(commit);
            }
        }
        return commits;
    }

}
