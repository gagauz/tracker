package com.gagauz.tracker.web.components.task;

import com.gagauz.tracker.beans.cvs.CvsService;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TaskCommits {

    @Parameter
    private Task task;

    private List<Commit> commits;

    @Property
    private Commit commit;

    @Inject
    private CvsService cvsService;

    public List<Commit> getCommits() {
        if (null == commits) {
            commits = cvsService.getCommits(task);
        }
        return commits;
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
}
