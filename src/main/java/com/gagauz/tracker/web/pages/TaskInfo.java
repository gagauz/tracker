package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.cvs.CvsService;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured
public class TaskInfo {

    @Property(write = false)
    private Task task;

    @Property
    private Stage stage;

    @Inject
    private CvsService cvsService;

    @Inject
    private StageDao stageDao;

    @Inject
    private ComponentResources componentResources;

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

    String onGetCommits(Task task) {
        StringBuilder sb = new StringBuilder();
        for (Commit c : cvsService.getCommits(task)) {
            sb
                    .append("<div>")
                    .append(c.getDate()).append(" ")
                    .append(c.getHash()).append(" ")
                    .append(c.getAuthor()).append(" ")
                    .append(c.getComment())
                    .append("</div>\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    @Cached
    public List<Stage> getStages() {
        return task.getStages();
    }

    public String getAjaxUrl() {
        return componentResources.createEventLink("getCommits", task).toAbsoluteURI();
    }

}
