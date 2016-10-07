package com.gagauz.tracker.beans.scenarios;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.TicketStatus;

@Service
public class ScStatus extends DataBaseScenario {

    @Autowired
    private TicketStatusDao statusDao;

    @Autowired
    private ScVersion scVersion;

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] { this.scVersion };
    }

    @Override
    protected void execute() {
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

        this.statusDao.flush();
    }

    private TicketStatus create(String name, String description, Collection<TicketStatus>... lists) {
        TicketStatus status = new TicketStatus();
        status.setName(name);
        status.setDescription(description);
        if (lists.length > 0) {
            status.setAllowedTo(lists[0]);
        }
        this.statusDao.save(status);
        return status;
    }

}
