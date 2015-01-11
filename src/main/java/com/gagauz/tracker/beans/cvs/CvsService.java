package com.gagauz.tracker.beans.cvs;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagauz.tracker.beans.dao.CommitDao;
import com.gagauz.tracker.db.model.Commit;

import edu.nyu.cs.javagit.api.DotGit;
import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;

@Service
public class CvsService {
    private static final DateFormat gitFormat = new GitDateFormat();

    @Autowired
    private CommitDao commitDao;

    private boolean inited;
    private String gv;
    private Date lastCommitDate;
    private DotGit dotGit;

    public void init() {
        try {
            //            JavaGitConfiguration.setGitPath("/usr/bin/");
            gv = JavaGitConfiguration.getGitVersion();
            File repositoryDirectory = new File("R:/projects-my/tracker/");

            //get the instance of the dotGit Object
            dotGit = DotGit.getInstance(repositoryDirectory);

            //Initialize the repository ,similar to git init
            dotGit.init();
        } catch (JavaGitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            inited = true;
        }
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
                commit.setComment(c.getMessage());
                commit.setHash(c.getSha());
                commits.add(commit);

                System.out.println(c.getDateString() + " " + c.getSha() + " " + c.getAuthor() + " " + c.getMessage());
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
