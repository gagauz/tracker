package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDao extends AbstractDao<Integer, Task> {

    @SuppressWarnings("unchecked")
    public List<Task> findByFeatureVersion(FeatureVersion featureVersion) {
        return getSession().createQuery("from Task t where featureVersion=:featureVersion").setEntity("featureVersion", featureVersion).list();
    }

}
