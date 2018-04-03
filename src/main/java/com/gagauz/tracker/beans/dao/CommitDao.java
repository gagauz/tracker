package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Commit;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class CommitDao extends AbstractHibernateDao<String, Commit> {

    public List<Commit> findByKey(String key) {
        return getSession().createQuery("from Commit t where comment like '%:key%'").setString("key", key).list();
    }
}
