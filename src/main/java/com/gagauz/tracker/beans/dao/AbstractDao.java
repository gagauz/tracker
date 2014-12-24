package com.gagauz.tracker.beans.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

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

    public void save(Entity entity) {
        getSession().saveOrUpdate(entity);
    }

}
