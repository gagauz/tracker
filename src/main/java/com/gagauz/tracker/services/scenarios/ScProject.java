package com.gagauz.tracker.services.scenarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.CvsRepo;
import com.gagauz.tracker.db.model.CvsType;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.xl0e.testdata.DataBaseScenario;

@Service
public class ScProject extends DataBaseScenario {

    public static final String TRACKER = "TRACKER";

    @Autowired
    private ProjectDao projectDao;

    @Override
    protected void execute() {
        Project project = new Project();
        project.setCode(TRACKER);
        project.setName("Трекер");
        projectDao.saveNoCommit(project);

        CvsRepo repo = new CvsRepo();
        if (true) {
            repo.setType(CvsType.GIT);
            repo.setUrl("https://github.com/gagauz/tracker.git");
            repo.setUsername("gagauz");
            repo.setPassword("p35neog0d");
            repo.setBranch("master");
        } else {
            repo.setType(CvsType.SVN);
            repo.setUrl("file:///R:/projects-my/tracker-svn-repo");
            // repo.setBranch("trunk");
        }
        project.setCvsRepo(repo);
        projectDao.save(project);
    }
}
