package com.gagauz.tracker.beans.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import com.gagauz.tracker.db.utils.Param;

//@Transactional
public class AbstractDao<I extends Serializable, E> extends org.gagauz.hibernate.dao.AbstractDao<I, E> {

    @SuppressWarnings("unchecked")
    public List<E> findByQuery(String hql, Param... params) {
        Query query = getSession().createQuery(hql);
        for (Param param : params) {
            param.update(query);
        }
        return query.list();
    }

}
