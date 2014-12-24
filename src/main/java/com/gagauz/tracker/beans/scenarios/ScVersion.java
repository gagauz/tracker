package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScVersion")
public class ScVersion extends DataBaseScenario {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private BugDao bugDao;

    @Autowired
    private ScUser scUser;

    @Override
    protected void execute() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        for (int i = 0; i < 1; i++) {
            Project p = new Project();
            projectDao.save(p);
            for (int j = 0; j < 2; j++) {
                Version v = new Version();
                v.setProject(p);
                v.setVersion("1." + j + "-SNAPSHOT");
                versionDao.save(v);

                for (int k = 0; k < 10; k++) {
                    Task t = new Task();
                    t.setVersion(v);
                    t.setCreator(user1);
                    t.setCreator(user2);

                    taskDao.save(t);
                }
            }
        }
    }

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] {scUser};
    }
}
