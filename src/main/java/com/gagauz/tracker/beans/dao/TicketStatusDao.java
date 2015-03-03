package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gagauz.tracker.db.utils.Param.param;

@Service
public class TicketStatusDao extends AbstractDao<Integer, TicketStatus> {

    public List<TicketStatus> findByProject(Project project) {
        return findByQuery("from TicketStatus v where project is null or project=:project", param("project", project));
    }

    public List<TicketStatus> findFirstByProject(Project project) {
        return findByQuery("from TicketStatus v where project is null or project=:project", param("project", project));
    }

}
