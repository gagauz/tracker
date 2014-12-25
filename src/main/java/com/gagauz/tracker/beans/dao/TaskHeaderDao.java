package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TaskHeader;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gagauz.tracker.db.utils.Param.param;

@Service
public class TaskHeaderDao extends AbstractDao<Integer, TaskHeader> {

    public List<TaskHeader> findByProject(Project project) {
        return findByQuery("from TaskHeader v where project=:project", param("project", project));
    }

}
