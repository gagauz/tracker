package com.gagauz.tracker.db.utils;

import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.utils.Serializer;

public class RoleSerializer implements Serializer<Role> {

    @Override
    public String serialize(Role object) {
        return null != object ? object.name() : null;
    }

    @Override
    public Role unserialize(String string) {
        return null != string ? Role.valueOf(string) : null;
    }

}
