package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.cvs.CvsService;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import java.util.List;

@Secured
public class TaskInfo {

    @Component(parameters = {"id=literal:commitsZone"})
    private Zone zone;

    @Property(write = false)
    private Task task;

    @Property
    private Stage stage;

    @Property
    private List<Commit> commits;

    @Property
    private Commit commit;

    @Inject
    private CvsService cvsService;

    @Inject
    private StageDao stageDao;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

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

    Object onGetCommits(Task task) {
        commits = cvsService.getCommits(task);
        return zone.getBody();
    }

    public String formatDetails(String details) {
        if (null != details) {
            return details
                    .replace("\n", "</div>")
                    .replace("A\t", "<div class=\"A\">")
                    .replace("M\t", "<div class=\"M\">")
                    .replace("D\t", "<div class=\"D\">");
        }

        return "";
    }

    @Cached
    public List<Stage> getStages() {
        return task.getStages();
    }

    public String getAjaxUrl() {
        return componentResources.createEventLink("getCommits", task).toRedirectURI();
    }

}
