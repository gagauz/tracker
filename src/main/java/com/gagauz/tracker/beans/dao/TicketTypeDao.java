package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketType;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gagauz.tracker.db.utils.Param.param;

@Service
public class TicketTypeDao extends AbstractDao<Integer, TicketType> {

    public List<TicketType> findByProject(Project project) {
        return findByQuery("from TicketType v where project is null or project=:project", param("project", project));
    }

}
