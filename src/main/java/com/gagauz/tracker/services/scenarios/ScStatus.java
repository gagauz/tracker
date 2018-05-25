package com.gagauz.tracker.services.scenarios;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;
import com.xl0e.testdata.DataBaseScenario;

@Service
public class ScStatus extends DataBaseScenario {

    @Autowired
    private TicketStatusDao statusDao;

    @Autowired
    private ProjectDao projectDao;

    private Project project;

    @Override
    protected List<Class<? extends DataBaseScenario>> getDependsOnClasses() {
        return Arrays.asList(ScProject.class);
    }

    @Override
    protected void execute() {
        project = projectDao.findByCode(ScProject.TRACKER);
        assert (project != null);
        TicketStatus open = create("Open", null);
        TicketStatus reopen = create("Reopen", null);
        TicketStatus inprog = create("In progress", null);

        TicketStatus done = create("Done", null);
        TicketStatus invalid = create("Invalid", null);
        TicketStatus incomplete = create("Incomplete", null);

        TicketStatus verified = create("Verified", null);
        TicketStatus closed = create("Closed", null);
        open.setAllowedTo(Arrays.asList(inprog, invalid, incomplete));

        reopen.setAllowedTo(open.getAllowedTo());

        inprog.setAllowedTo(Arrays.asList(done, invalid, incomplete));

        done.setAllowedTo(Arrays.asList(reopen, verified, closed));

        invalid.setAllowedTo(Arrays.asList(reopen, closed));

        verified.setAllowedTo(Arrays.asList(reopen, closed));

        closed.setAllowedTo(Arrays.asList(reopen));

        statusDao.flush();
    }

    private TicketStatus create(String name, String description, Collection<TicketStatus>... lists) {
        TicketStatus status = new TicketStatus();
        status.setName(name);
        status.setDescription(description);
        status.setProject(project);
        status.setCss(name.toLowerCase().replace(' ', '_'));
        if (lists.length > 0) {
            status.setAllowedTo(lists[0]);
        }
        statusDao.save(status);
        return status;
    }

}
