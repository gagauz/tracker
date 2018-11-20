package com.gagauz.tracker.services.dao;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.TicketStatusToUserGroup;
import com.gagauz.tracker.db.model.TicketStatusToUserGroup.Id;

@Service
public class TicketStatusToUserGroupDao extends AbstractDao<TicketStatusToUserGroup.Id, TicketStatusToUserGroup> {
    @Override
    protected Function<String, Id> getIdDeserializer() {
        return Id::fromString;
    }
}
