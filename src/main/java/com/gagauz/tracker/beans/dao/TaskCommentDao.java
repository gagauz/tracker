package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCommentDao extends AbstractDao<Integer, TaskComment> {

    public List<TaskComment> findByTask(Task task) {
        return getSession().createQuery("from TaskComment t where task=:task").setEntity("task", task).list();
    }
}
