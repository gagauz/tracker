package com.gagauz.tracker.services.dao;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.TicketTypeToUserGroup;
import com.gagauz.tracker.db.model.TicketTypeToUserGroup.Id;

@Service
public class TicketTypeToUserGroupDao extends AbstractDao<TicketTypeToUserGroup.Id, TicketTypeToUserGroup> {
    @Override
    protected Function<String, Id> getIdDeserializer() {
        return Id::fromString;
    }
}
