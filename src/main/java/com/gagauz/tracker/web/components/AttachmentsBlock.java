package com.gagauz.tracker.web.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.gagauz.tracker.db.model.Attachment;

public class AttachmentsBlock {

    @Parameter
    @Property
    private List<Attachment> attachments;

    @Property
    private Attachment attachment;
}
