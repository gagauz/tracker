package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.StageActionDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.StageTriggerDao;
import com.gagauz.tracker.db.model.*;
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

    @Property
    private AbstractStageAction action;

    @Property
    private StageTrigger trigger;

    @Inject
    private StageDao stageDao;

    @Inject
    private StageTriggerDao stageTriggerDao;

    @Inject
    private StageActionDao stageActionDao;

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

    void onCreateTrigger() {
        StageTrigger trigger = new StageTrigger();
        stageTriggerDao.save(trigger);
        stage.getTriggers().add(trigger);
    }

    void onCreateBeforeAction() {
        BeforeAction action = new BeforeAction();
        //stageActionDao.save(action);
        stage.getBeforeActions().add(action);
    }

    void onCreateAfterAction() {
        AfterAction action = new AfterAction();
        //stageActionDao.save(action);
        stage.getAfterActions().add(action);
    }

    void onCreateStageAction() {
        StageAction action = new StageAction();
        stageActionDao.save(action);
        stage.getStageActions().add(action);
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
