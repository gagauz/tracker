package com.gagauz.tracker.services.scenarios;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.StageDao;
import com.xl0e.testdata.DataBaseScenario;

// @Service
public class ScScheduler extends DataBaseScenario {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private StageDao stageDao;

    @Override
    protected List<Class<? extends DataBaseScenario>> getDependsOnClasses() {
        return Arrays.asList(ScVersion.class);
    }

    @Override
    protected void execute() {
        for (Project p : projectDao.findAll()) {
            Stage s = new Stage();
            s.setName("Build");
            s.setDescription("Build project");
            s.setProject(p);
            stageDao.save(s);

        }
    }
}
