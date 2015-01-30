package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

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

    @Inject
    private StageDao stageDao;

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

    @Cached
    public List<Stage> getStages() {
        return task.getStages();
    }

}
