package com.gagauz.tracker.beans.scenarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;

@Service
public class ScRoles extends DataBaseScenario {

    public static final String PRODUCT_OWNER = "Product owner";

    public static final String QA_LEAD = "QA lead";
    public static final String QA = "QA";

    public static final String DEV = "Developer";
    public static final String SA_LEAD = "SA lead";
    public static final String SA = "System administrator ";

    public static final String DEV_LEAD = "Team lead";

    public static final String BUSSINES_ANALYST = "Bussines analyst";

    public static final String BUSSINES_OWNER = "Bussines owner";

    public static final String PROJECT_OWNER = "Project owner";

    @Autowired
    private ScProject scProject;

    @Autowired
    private RoleGroupDao roleGroupDao;

    @Autowired
    private ProjectDao projectDao;

    private Project project;

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] { scProject };
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);

        create(PROJECT_OWNER);
        create(BUSSINES_OWNER);
        create(PRODUCT_OWNER);
        create(BUSSINES_ANALYST);
        create(DEV_LEAD);
        create(DEV);
        create(SA_LEAD);
        create(SA);
        create(QA_LEAD);
        create(QA);

        roleGroupDao.flush();
    }

    private RoleGroup create(String name) {
        RoleGroup group = new RoleGroup();
        group.setProject(project);
        group.setName(name);
        roleGroupDao.saveNoCommit(group);
        return group;
    }

}
