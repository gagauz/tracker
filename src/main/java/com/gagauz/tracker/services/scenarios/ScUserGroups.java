package com.gagauz.tracker.services.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.UserDao;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.xl0e.testdata.DataBaseScenario;
import com.xl0e.util.C;

@Service
public class ScUserGroups extends DataBaseScenario {

    @Autowired
    private UserGroupDao roleGroupDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserDao userDao;

    private Project project;

    @Override
    protected List<Class<? extends DataBaseScenario>> getDependsOnClasses() {
        return Arrays.asList(ScProject.class, ScUser.class);
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);

        Set<UserGroup> userGroups = C.hashSet();

        userGroups.add(create("Product owner"));
        userGroups.add(create("BA"));
        userGroups.add(create("Developer"));
        userGroups.add(create("DevOps"));
        userGroups.add(create("Solution architect"));
        userGroups.add(create("QA"));

        userDao.findAll().forEach(u -> {
            u.setUserGroups(userGroups);
            userDao.saveNoCommit(u);
        });

        roleGroupDao.flush();
        userDao.flush();
    }

    private UserGroup create(String name) {
        UserGroup group = new UserGroup();
        group.setProject(project);
        group.setName(name);
        roleGroupDao.saveNoCommit(group);
        return group;
    }

}
