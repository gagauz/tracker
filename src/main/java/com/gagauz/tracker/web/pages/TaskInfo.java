package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tapestry.security.SecurityUserCreator;
import com.gagauz.tracker.beans.cvs.CvsService;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.TaskCommentDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;
import com.gagauz.tracker.db.model.User;

@Secured
public class TaskInfo {

    @Property(write = false)
    private Task task;

    @Persist
    @Property
    private boolean edit;

    @SessionState
    private User user;

    @Inject
    private TaskDao taskDao;

    @Property
    private Stage stage;

    @Property
    private List<Commit> commits;

    @Property
    private Commit commit;

    @Property
    private List<TaskComment> comments;

    @Property
    private TaskComment comment;

    @Inject
    private CvsService cvsService;

    @Inject
    private StageDao stageDao;

    @Inject
    private TaskCommentDao taskCommentDao;

    Object onActivate(Task task) {
        if (null == task) {
            return Index.class;
        }
        this.task = task;

        return null;
    }

    Object onPassivate() {
        return task;
    }

    public boolean isOwner() {
        return user.getId() == task.getOwner().getId();
    }

    void onGetCommits(Task task) {
        commits = cvsService.getCommits(task);
    }

    void onGetComments(Task task) {
        comments = taskCommentDao.findByTask(task);
    }

    public String formatDetails(String details) {
        if (null != details) {
            return details
                    .replace("\n", "</li>")
                    .replace("A\t", "<li class=\"a\">A ")
                    .replace("M\t", "<li class=\"m\">M ")
                    .replace("D\t", "<li class=\"d\">D ");
        }

        return "";
    }

    @Cached
    public List<Stage> getStages() {
        return task.getStages();
    }

    private TaskComment newComment;

    @Inject
    private SecurityUserCreator sessionUserCreator;

    public TaskComment getNewComment() {
        if (null == newComment) {
            newComment = new TaskComment();
            newComment.setUser((User) sessionUserCreator.getUserFromContext());
        }
        return newComment;
    }
}
