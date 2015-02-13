package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.StageTrigger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageTriggerDao extends AbstractDao<Integer, StageTrigger> {

    public List<StageTrigger> findByProject(Project project) {
        return createQuery("from StageTrigger v where project=:project").setEntity("project", project).list();
    }

    public List<StageTrigger> findEnabled() {
        return createQuery("from StageTrigger v where enabled=true").list();
    }

}
