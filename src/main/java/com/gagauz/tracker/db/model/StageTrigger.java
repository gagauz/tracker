package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StageTrigger {
    private String data;
    private String cron;
    private boolean enabled = true;
    private Type type;

    @Column(nullable = true)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(nullable = true)
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Column
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        SCRIPT;
    }
}
