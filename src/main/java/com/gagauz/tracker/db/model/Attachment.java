package com.gagauz.tracker.db.model;

public class Attachment {

    private String value;

    public Attachment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isImage() {
        return value.endsWith(".jpg") || value.endsWith(".png") || value.endsWith(".gif");
    }
}
