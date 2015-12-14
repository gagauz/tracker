package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.TicketTypeDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class ScStatus extends DataBaseScenario {

    @Autowired
    private TicketStatusDao statusDao;

    @Autowired
    private TicketTypeDao typeDao;

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
        open.setAllowedTo(Arrays.asList(inprog));

        reopen.setAllowedFrom(Arrays.asList(done, cant, invalid, duplicate, closed));
        reopen.setAllowedTo(open.getAllowedTo());

        inprog.setAllowedFrom(Arrays.asList(open, reopen));
        inprog.setAllowedTo(Arrays.asList(done, cant, invalid, duplicate));

        done.setAllowedFrom(Arrays.asList(inprog, reopen, open));
        done.setAllowedTo(Arrays.asList(reopen, closed));

        cant.setAllowedFrom(done.getAllowedFrom());
        cant.setAllowedTo(done.getAllowedTo());

        invalid.setAllowedFrom(done.getAllowedFrom());
        invalid.setAllowedTo(done.getAllowedTo());

        duplicate.setAllowedFrom(done.getAllowedFrom());
        duplicate.setAllowedTo(done.getAllowedTo());

        closed.setAllowedFrom(Arrays.asList(done, cant, invalid, duplicate));
        closed.setAllowedTo(Arrays.asList(reopen));

        statusDao.flush();

        create("Task");
        create("Bug");
    }

    private TicketStatus create(String name, String description, Collection<TicketStatus>... lists) {
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

    private TicketType create(String name) {
        TicketType type = new TicketType();
        type.setName(name);
        typeDao.save(type);
        return type;
    }

}
