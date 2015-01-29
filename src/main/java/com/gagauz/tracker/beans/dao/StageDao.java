package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Build;
import com.gagauz.tracker.db.model.Stage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageDao extends AbstractDao<Integer, Stage> {

    @SuppressWarnings("unchecked")
    public List<Stage> findByProject(Build project) {
        return createQuery("from Stage v where project=:project").setEntity("project", project).list();
    }

}
