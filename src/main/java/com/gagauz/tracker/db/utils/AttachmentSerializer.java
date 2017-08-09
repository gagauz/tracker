package com.gagauz.tracker.db.utils;

import com.gagauz.tracker.db.model.Attachment;
import com.xl0e.hibernate.model.base.Serializer;

public class AttachmentSerializer implements Serializer<Attachment> {

    @Override
    public String serialize(Attachment object) {
        return null != object ? object.getValue() : null;
    }

    @Override
    public Attachment unserialize(String string, Class<Attachment> clazz) {
        return null != string ? new Attachment(string) : null;
    }

}
