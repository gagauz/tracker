package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class ScStatus extends DataBaseScenario {

    @Autowired
    private TicketStatusDao statusDao;

    @Override
    protected void execute() {
        TicketStatus open = create("Open", null);
        TicketStatus reopen = create("Reopen", null);
        TicketStatus inprog = create("In progress", null);

        TicketStatus done = create("Done", null);
        TicketStatus cant = create("Cannot reproduce", null);
        TicketStatus invalid = create("Invalid", null);
        TicketStatus duplicate = create("Duplicate", null);

        TicketStatus closed = create("Closed", null);
        open.getAllowedFrom().add(null);
        open.setAllowedTo(Arrays.asList(inprog.getId()));

        reopen.setAllowedFrom(Arrays.asList(done.getId(), cant.getId(), invalid.getId(), duplicate.getId(), closed.getId()));
        reopen.setAllowedTo(open.getAllowedTo());

        inprog.setAllowedFrom(Arrays.asList(open.getId(), reopen.getId()));
        inprog.setAllowedTo(Arrays.asList(done.getId(), cant.getId(), invalid.getId(), duplicate.getId()));

        done.setAllowedFrom(Arrays.asList(inprog.getId(), reopen.getId(), open.getId()));
        done.setAllowedTo(Arrays.asList(reopen.getId(), closed.getId()));

        cant.setAllowedFrom(done.getAllowedFrom());
        cant.setAllowedTo(done.getAllowedTo());

        invalid.setAllowedFrom(done.getAllowedFrom());
        invalid.setAllowedTo(done.getAllowedTo());

        duplicate.setAllowedFrom(done.getAllowedFrom());
        duplicate.setAllowedTo(done.getAllowedTo());

        closed.setAllowedFrom(Arrays.asList(done.getId(), cant.getId(), invalid.getId(), duplicate.getId()));
        closed.setAllowedTo(Arrays.asList(reopen.getId()));
    }

    private TicketStatus create(String name, String description, Collection<Integer>... lists) {
        TicketStatus status = new TicketStatus();
        status.setName(name);
        status.setDescription(description);
        if (lists.length > 0) {
            status.setAllowedFrom(lists[0]);
        }
        if (lists.length > 1) {
            status.setAllowedFrom(lists[1]);
        }
        statusDao.save(status);
        return status;
    }

}
