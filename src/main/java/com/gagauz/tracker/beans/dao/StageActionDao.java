package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.StageAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageActionDao extends AbstractDao<Integer, StageAction> {

    public List<StageAction> findByProject(Project project) {
        return createQuery("from StageAction v where project=:project").setEntity("project", project).list();
    }

}
