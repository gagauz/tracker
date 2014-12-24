package com.gagauz.tracker.beans.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.TypeVariable;

public class AbstractDao<Id extends Serializable, Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    protected Class<Entity> entityClass;

    public AbstractDao() {
        for (TypeVariable<?> typeVar : this.getClass().getTypeParameters()) {
            System.out.println(typeVar);
        }
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Entity findById(Id id) {
        return (Entity) getSession().get(entityClass, id);
    }

    public void save(Entity id) {
        getSession().saveOrUpdate(entityClass);
    }

}
