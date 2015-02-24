package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.db.model.Stage;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured
public class StageInfo {

    @Property(write = false)
    private Stage stage;

    @Property
    private Stage stageRow;

    @Inject
    private StageDao stageDao;

    @Inject
    private SchedulerService schedulerService;

    Object onActivate(Stage stage) {
        if (null == stage) {
            return Index.class;
        }
        this.stage = stage;

        return null;
    }

    Object onPassivate() {
        return stage;
    }

    void onSuccessFromEditForm() {
        stageDao.save(stage);
    }

    @Cached
    public List<Stage> getChildStages() {
        return stageDao.findByParent(stage);
    }

    //    @Cached
    //    public Map<Version, List<Task>> getMap() {
    //        Map<Version, List<Task>> map = new HashMap<Version, List<Task>>(feature.getFeatureVersions().size());
    //        for (Task task : taskDao.findByFeature(feature)) {
    //            List<Task> tasks = map.get(task.getVersion());
    //            if (null == tasks) {
    //                tasks = new LinkedList<Task>();
    //                map.put(task.getVersion(), tasks);
    //            }
    //            tasks.add(task);
    //        }
    //        return map;
    //    }
}
