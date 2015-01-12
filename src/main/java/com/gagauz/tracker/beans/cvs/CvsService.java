package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.beans.dao.CommitDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Project;
import edu.nyu.cs.javagit.api.DotGit;
import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class CvsService {
    private static final DateFormat gitFormat = new GitDateFormat();

    @Autowired
    private CommitDao commitDao;

    @Autowired
    private ProjectDao projectDao;

    private boolean inited;
    private String gv;
    private Date lastCommitDate;
    private Map<Project, DotGit> dotGitMap = new HashMap<Project, DotGit>();

    public void init() {
        for (Project project : projectDao.findAll()) {
            initProjectRepo(project);
        }
    }

    private DotGit initProjectRepo(Project project) {
        try {
            gv = JavaGitConfiguration.getGitVersion();
            File repositoryDirectory = new File(project.getCvsRepositoryPath());
            DotGit dotGit = DotGit.getInstance(repositoryDirectory);
            dotGit.init();
            dotGitMap.put(project, dotGit);
            return dotGit;
        } catch (JavaGitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            inited = true;
        }
        return null;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void update() {
        System.out.println("update " + gv);
        if (!inited) {
            init();
        }
        try {
            List<Commit> commits = new ArrayList<Commit>();
            for (Project project : projectDao.findAll()) {
                DotGit dotGit = dotGitMap.get(project);
                if (null == dotGit) {
                    dotGit = initProjectRepo(project);
                }
                GitLogOptions opts = new GitLogOptions();
                if (lastCommitDate != null) {
                    opts.setOptLimitCommitSince(true, gitFormat.format(lastCommitDate));
                }
                for (edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit c : dotGit.getLog(opts)) {
                    Commit commit = new Commit();
                    Date d = gitFormat.parse(c.getDateString());
                    if (null == lastCommitDate || lastCommitDate.before(d)) {
                        lastCommitDate = d;
                    }
                    commit.setDate(d);
                    commit.setAuthor(c.getAuthor());
                    commit.setComment(c.getMessage().trim());
                    commit.setHash(c.getSha());
                    commits.add(commit);

                    System.out.println(c.getDateString() + " " + c.getSha() + " " + c.getAuthor() + " " + c.getMessage());
                }
            }

            commitDao.save(commits);
        } catch (JavaGitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Date d;
        try {
            d = gitFormat.parse("Tue Dec 30 18:36:55 2014 +0300");
            System.out.println(gitFormat.format(d));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
