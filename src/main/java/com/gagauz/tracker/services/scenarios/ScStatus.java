package com.gagauz.tracker.services.scenarios;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketStatusToUserGroup;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;
import com.gagauz.tracker.services.dao.TicketStatusToUserGroupDao;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.xl0e.testdata.DataBaseScenario;

@Service
public class ScStatus extends DataBaseScenario {

    @Autowired
    private TicketStatusDao statusDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private TicketStatusToUserGroupDao ticketStatusToUserGroupDao;

    private Project project;

    private List<UserGroup> roles;

    @Override
    protected List<Class<? extends DataBaseScenario>> getDependsOnClasses() {
        return Arrays.asList(ScProject.class, ScUserGroups.class);
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);
        assert (project != null);
        TicketStatus closed = create("Closed");
        allowTtransition(closed, "QA");
        TicketStatus reopen = create("Reopened");
        allowTtransition(reopen, "QA", "Developer");

        TicketStatus intest = create("In test", closed, reopen);
        allowTtransition(intest, "QA");

        TicketStatus readytest = create("Ready for test", intest);
        allowTtransition(readytest, "Developer");

        TicketStatus indev = create("In development", readytest);
        allowTtransition(indev, "Developer");

        TicketStatus open = create("Open", indev);

        reopen.setAllowedTo(Arrays.asList(indev, readytest));

        statusDao.flush();
        ticketStatusToUserGroupDao.flush();
    }

    private void allowTtransition(TicketStatus status, String... groups) {
        for (String group : groups) {
            TicketStatusToUserGroup transition = new TicketStatusToUserGroup();
            transition.getId().setTicketStatusId(status.getId());
            transition.getId().setUserGroupId(getByGroup(group).getId());
            ticketStatusToUserGroupDao.saveNoCommit(transition);
        }
    }

    UserGroup getByGroup(String name) {
        if (null == roles) {
            roles = userGroupDao.findByProject(project);
        }
        return roles.stream().filter(g -> g.getName().equals(name)).findFirst().get();
    }

    private TicketStatus create(String name, TicketStatus... lists) {
        TicketStatus status = new TicketStatus();
        status.setName(name);
        status.setDescription(name);
        status.setProject(project);
        status.setCss(name.toLowerCase().replace(' ', '_'));
        if (lists.length > 0) {
            status.setAllowedTo(Arrays.asList(lists));
        }
        statusDao.save(status);
        return status;
    }

}
