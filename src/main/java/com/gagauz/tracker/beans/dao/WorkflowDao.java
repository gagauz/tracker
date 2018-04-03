package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Workflow;


@Service
public class WorkflowDao extends AbstractDao<Integer, Workflow> {

    public List<Workflow> findCommentsByTicket(Ticket ticket) {
        return getSession()
                .createQuery(
                        "from Workflow t join fetch t.author ta where t.ticket=:ticket and ta is not null and t.comment is not null order by created asc")
                .setEntity("ticket", ticket).list();
    }

}
