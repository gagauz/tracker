package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Commit;


@Service
public class CommitDao extends AbstractDao<String, Commit> {

    public List<Commit> findByKey(String key) {
        return getSession().createQuery("from Commit t where comment like '%:key%'").setString("key", key).list();
    }
}
