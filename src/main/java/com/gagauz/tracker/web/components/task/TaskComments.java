package com.gagauz.tracker.web.components.task;

import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.beans.dao.TaskCommentDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.List;

public class TaskComments {

    @Component
    private Form commentForm;

    @Parameter
    private Task task;

    @Parameter
    private Zone zone;

    @Component(parameters = {"id=literal:editZone", "show=popup", "update=popup"})
    private Zone editZone;

    private List<TaskComment> comments;

    @Property
    private TaskComment comment;

    //    @Persist
    //("flash")
    private TaskComment newComment;

    @SessionState(create = false)
    private SecurityUser securityUser;

    @Inject
    private TaskCommentDao taskCommentDao;

    @Inject
    private Request request;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    Object onSubmitFromCommentForm(int id) {
        if (!commentForm.getHasErrors()) {
            newComment.setId(id);
            newComment.setAuthor((User) securityUser);
            newComment.setTask(task);
            taskCommentDao.save(newComment);
            newComment = null;
            javaScriptSupport.addInitializerCall("$j('%s').parent('popup').trigger('popupHide');", editZone.getClientId());
        }

        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    Object onEdit(TaskComment comment) {
        if (request.isXHR()) {
            newComment = comment;
            return editZone.getBody();
        }
        return null;
    }

    Object onDrop(TaskComment comment) {
        taskCommentDao.delete(comment);
        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    public List<TaskComment> getComments() {
        if (null != task && null == comments) {
            comments = taskCommentDao.findByTask(task);
        }
        return comments;
    }

    public TaskComment getNewComment() {
        if (null == newComment) {
            newComment = new TaskComment();
        }
        return newComment;
    }

    public boolean isUserComment() {
        return null != securityUser && comment.getAuthor().equals(securityUser);
    }

    public String getParentZoneId() {
        return null != zone ? zone.getClientId() : null;
    }
}
