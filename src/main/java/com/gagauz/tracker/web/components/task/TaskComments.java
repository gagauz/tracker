package com.gagauz.tracker.web.components.task;

import com.gagauz.tapestry.security.SecurityUserCreator;
import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.beans.dao.TaskCommentDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TaskComments {

    @Parameter
    private Task task;

    private List<TaskComment> comments;

    @Property
    private TaskComment comment;

    private TaskComment newComment;

    @Inject
    private SecurityUserCreator sessionUserCreator;

    @SessionState(create = false)
    private SecurityUser securityUser;

    @Inject
    private TaskCommentDao taskCommentDao;

    void onSuccessFromCommentForm() {
        taskCommentDao.save(newComment);
    }

    public List<TaskComment> getComments() {
        if (null == comments) {
            comments = taskCommentDao.findByTask(task);
        }
        return comments;
    }

    public TaskComment getNewComment() {
        if (null == newComment) {
            newComment = new TaskComment();
            newComment.setUser((User) sessionUserCreator.getUserFromContext());
            newComment.setTask(task);
        }
        return newComment;
    }

    public boolean isUserComment() {
        System.out.println(securityUser);
        return securityUser != null && securityUser.equals(comment.getUser());
    }
}
