package com.gagauz.tracker.services.scenarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.cvs.ProjectRepository;
import com.gagauz.tracker.db.model.cvs.Provider;
import com.gagauz.tracker.db.model.cvs.Repository;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.cvs.RepositoryDao;
import com.xl0e.testdata.DataBaseScenario;

@Service
public class ScProject extends DataBaseScenario {

    public static final String TRACKER = "TRACKER";

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private RepositoryDao repositoryDao;

    @Override
    protected void execute() {
        Project project = new Project();
        project.setCode(TRACKER);
        project.setName("Трекер");
        projectDao.saveNoCommit(project);

        Repository repo = new Repository();
        repo.setProvider(Provider.BITBUCKET);
        repo.setName("xl0e");

        ProjectRepository projectrepo = new ProjectRepository();
        projectrepo.setProject(project);
        projectrepo.setRepository(repo);
        projectrepo.setName("tracker");
        project.setProjectRepository(projectrepo);
        repositoryDao.save(repo);
        projectDao.save(project);
    }
}
