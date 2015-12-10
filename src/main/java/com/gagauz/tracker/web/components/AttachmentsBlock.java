package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Attachment;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import java.util.List;

@Import(module = "jquery")
public class AttachmentsBlock {

    @Parameter
    @Property
    private List<Attachment> attachments;

    @Property
    private Attachment attachment;
}
