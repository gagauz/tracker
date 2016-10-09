package com.gagauz.tracker.beans.scenarios;

import static com.gagauz.tracker.beans.scenarios.ScRoles.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.TicketTypeDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.TicketType;
@Service
public class ScTicketType extends DataBaseScenario {

    @Autowired
    private ScProject scProject;

    @Autowired
    private ScRoles scRoles;

    @Autowired
    private TicketTypeDao ticketTypeDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private RoleGroupDao roleGroupDao;

    private Project project;

    private List<RoleGroup> roles;

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] { scProject, scRoles };
    }

    RoleGroup getByName(String name) {
        if (null == roles ) {
            roles = roleGroupDao.findByProject(project);
        }
        return roles.stream().filter(g -> g.getName().equals(name)).findFirst().get();
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);

        TicketType feature1 = create("Feature", project, null, getByName(PROJECT_OWNER), getByName(BUSSINES_ANALYST));
        feature1.setCss("label-default");
        create("Dev task", project, feature1, getByName(DEV_LEAD), getByName(DEV)).setCss("label-success");
        create("SA task", project, feature1, getByName(SA_LEAD), getByName(SA)).setCss("label-warning");
        create("BA task", project, feature1, getByName(PROJECT_OWNER), getByName(BUSSINES_ANALYST)).setCss("label-primary");
        create("QA task", project, feature1, getByName(QA_LEAD), getByName(QA)).setCss("label-info");
        create("Bug", project, feature1, getByName(QA), getByName(DEV)).setCss("label-danger");

        ticketTypeDao.flush();
    }

    private TicketType create(String name, Project project, TicketType parent, RoleGroup... groups) {
        TicketType type = new TicketType();
        type.setName(name);
        type.setProject(project);
        type.setParent(parent);
        ticketTypeDao.saveNoCommit(type);
        if (groups.length > 0) {
            type.setCreator(groups[0]);
            if (groups.length > 1) {
                type.setAssignee(groups[1]);
            }
        }

        return type;
    }

}
