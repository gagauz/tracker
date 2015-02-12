package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Stage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageDao extends AbstractDao<Integer, Stage> {

    public List<Stage> findByProject(Project project) {
        return createQuery("from Stage v where project=:project").setEntity("project", project).list();
    }

    public List<Stage> findByProject(Project project, Integer id) {
        return createQuery("from Stage v where project=:project and id!=:id").setEntity("project", project).setInteger("id", id).list();
    }

    public List<Stage> findByParent(Stage parent) {
        return createQuery("from Stage v where parent=:parent").setEntity("parent", parent).list();
    }

}
