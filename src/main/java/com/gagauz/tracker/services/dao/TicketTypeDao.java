package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketType;


@Service
public class TicketTypeDao extends AbstractDao<Integer, TicketType> {

    public List<TicketType> findByProject(Project project) {
        return findByFilter(filter().or().isNull("project").eq("project", project));
    }

    public List<TicketType> findCommon() {
        return findByFilter(filter().or().isNull("project"));
    }

}
