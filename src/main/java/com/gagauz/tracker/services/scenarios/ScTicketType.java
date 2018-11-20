package com.gagauz.tracker.services.scenarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketTransition;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.TicketTypeToUserGroup;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.TicketTypeDao;
import com.gagauz.tracker.services.dao.TicketTypeToUserGroupDao;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.xl0e.testdata.DataBaseScenario;

@Service
public class ScTicketType extends DataBaseScenario {

    @Autowired
    private ScProject scProject;

    @Autowired
    private ScUserGroups scRoles;

    @Autowired
    private TicketTypeDao ticketTypeDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TicketTypeToUserGroupDao ticketTypeToUserGroupDao;

    @Autowired
    private UserGroupDao roleGroupDao;

    @Autowired
    private UserGroupDao userGroupDao;

    private Project project;

    private List<UserGroup> roles;

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] { scProject, scRoles };
    }

    UserGroup getByName(String name) {
        if (null == roles) {
            roles = roleGroupDao.findByProject(project);
        }
        return roles.stream().filter(g -> g.getName().equals(name)).findFirst().get();
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);

        TicketType feature1 = create("Feature", project, null);
        feature1.setCss("label-default");
        TicketType devtask = create("Dev task", project, feature1);
        allowCreate(devtask, "Developer");
        allowAssign(devtask, "Developer");

        TicketType qatask = create("QA task", project, feature1);

        allowCreate(qatask, "QA");
        allowAssign(qatask, "QA");

        TicketType bug = create("Bug", project, feature1);

        allowCreate(bug, "QA", "Developer");
        allowAssign(bug, "QA", "Developer");

        ticketTypeDao.flush();
        ticketTypeToUserGroupDao.flush();
    }

    private void allowCreate(TicketType type, String... groups) {
        for (String group : groups) {
            TicketTypeToUserGroup transition = new TicketTypeToUserGroup();
            transition.getId().setTransition(TicketTransition.CREATE);
            transition.getId().setTicketTypeId(type.getId());
            transition.getId().setUserGroupId(getByGroup(group).getId());
            ticketTypeToUserGroupDao.saveNoCommit(transition);
        }
    }

    private void allowAssign(TicketType type, String... groups) {
        for (String group : groups) {
            TicketTypeToUserGroup transition = new TicketTypeToUserGroup();
            transition.getId().setTransition(TicketTransition.ASSIGN);
            transition.getId().setTicketTypeId(type.getId());
            transition.getId().setUserGroupId(getByGroup(group).getId());
            ticketTypeToUserGroupDao.saveNoCommit(transition);
        }
    }

    UserGroup getByGroup(String name) {
        if (null == roles) {
            roles = userGroupDao.findByProject(project);
        }
        return roles.stream().filter(g -> g.getName().equals(name)).findFirst().get();
    }

    private TicketType create(String name, Project project, TicketType parent) {
        TicketType type = new TicketType();
        type.setName(name);
        type.setProject(project);
        type.setParent(parent);
        ticketTypeDao.saveNoCommit(type);

        return type;
    }

}
