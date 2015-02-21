package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.StageTriggerDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.StageTrigger;
import com.gagauz.tracker.db.model.StageTrigger.Type;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class ScScheduler extends DataBaseScenario {

    @Autowired
    private ScVersion scVersion;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private StageDao stageDao;

    @Autowired
    private StageTriggerDao stageTriggerDao;

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] {scVersion};
    }

    @Override
    protected void execute() {
        for (Project p : projectDao.findAll()) {
            Stage s = new Stage();
            s.setName("Build");
            s.setDescription("Build project");
            s.setProject(p);
            stageDao.save(s);

            StageTrigger t = new StageTrigger();
            t.setName("Scheduler every 10 sec");
            t.setData("git ");
            t.setParent(s);
            t.setEnabled(true);
            t.setType(Type.SCRIPT);
            t.setCron("0/10 * * ? * ?");
            stageTriggerDao.save(t);

        }
    }
}
