package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.db.model.Stage;
import org.gagauz.tracker.web.security.Secured;
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
    //    public Map<Version, List<Ticket>> getMap() {
    //        Map<Version, List<Ticket>> map = new HashMap<Version, List<Ticket>>(feature.getFeatureVersions().size());
    //        for (Ticket ticket : ticketDao.findByFeature(feature)) {
    //            List<Ticket> tickets = map.get(ticket.getVersion());
    //            if (null == tickets) {
    //                tickets = new LinkedList<Ticket>();
    //                map.put(ticket.getVersion(), tickets);
    //            }
    //            tickets.add(ticket);
    //        }
    //        return map;
    //    }
}
