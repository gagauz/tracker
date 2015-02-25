package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCommentDao extends AbstractDao<Integer, TicketComment> {

    public List<TicketComment> findByTicket(Ticket ticket) {
        return getSession().createQuery("from TicketComment t where ticket=:ticket order by updated asc").setEntity("ticket", ticket).list();
    }
}