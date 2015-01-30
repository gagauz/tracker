package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.utils.Param;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class AbstractDao<Id extends Serializable, Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    protected Class<Entity> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        entityClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Entity findById(Id id) {
        return (Entity) getSession().get(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<Entity> findByQuery(String hql, Param... params) {
        Query query = getSession().createQuery(hql);
        for (Param param : params) {
            param.update(query);
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public Query createQuery(String queryString) {
        return getSession().createQuery(queryString);
    }

    @SuppressWarnings("unchecked")
    public List<Entity> findAll() {
        return getSession().createCriteria(entityClass).list();
    }

    public void save(Entity entity) {
        getSession().saveOrUpdate(entity);
    }

    public void save(Collection<Entity> entities) {
        for (Entity entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }

    public void delete(Entity entity) {
        getSession().delete(entity);
    }

    public void evict(Entity entity) {
        getSession().evict(entity);
    }

}
