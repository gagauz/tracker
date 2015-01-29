package com.gagauz.tracker.web.components;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.beans.dao.TaskCommentDao;
import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;

public class TaskForm {

    @Parameter(name = "task")
    private Task taskParam;

    @Property(write = false)
    private Task task;

    @Property
    private Attachment attachment;

    @Property
    private List<TaskComment> comments;

    @Property
    private TaskComment comment;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private TaskCommentDao taskCommentDao;

    boolean setupRender() {
        if (null != taskParam) {
            task = taskParam;
        }
        return task != null;
    }

    public String getAjaxUrl() {
        return componentResources.createEventLink("getComments", task).toRedirectURI();
    }

    void onGetComments(Task task) {
        comments = taskCommentDao.findByTask(task);
    }
}
