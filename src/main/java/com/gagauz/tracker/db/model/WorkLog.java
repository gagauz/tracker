package com.gagauz.tracker.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.Identifiable;

@Entity
@Table(name = "work_log")
public class WorkLog implements Identifiable {
    private int id;
    private Task task;
    private int logTime;
    private String comment;

    @Override
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Column
    public int getLogTime() {
        return logTime;
    }

    public void setLogTime(int logTime) {
        this.logTime = logTime;
    }

    @Column
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
