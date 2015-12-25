package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.utils.Param;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Transactional
public class AbstractDao<I extends Serializable, E> {

    private static Map<Class, AbstractDao> MAP = new HashMap<>();

    @Autowired
    private SessionFactory sessionFactory;

    protected Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        MAP.put(entityClass, this);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSession(Session session) {

    }

    @SuppressWarnings("unchecked")
    public I getIdentifier(E entity) {
        return (I) getSession().getIdentifier(entity);
    }

    @SuppressWarnings("unchecked")
    public E findById(I id) {
        return (E) getSession().get(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<E> findByQuery(String hql, Param... params) {
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
    public List<E> findAll() {
        return getSession().createCriteria(entityClass).list();
    }

    public void merge(E entity) {
        getSession().merge(entity);
        getSession().flush();
    }

    public void save(E entity) {
        getSession().saveOrUpdate(entity);
        getSession().flush();
    }

    public void save(Collection<E> entities) {
        for (E entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }

    public void delete(E entity) {
        getSession().delete(entity);
    }

    public void evict(E entity) {
        getSession().evict(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public static <Id extends Serializable, En> AbstractDao<Id, En> getDao(Class<En> eClass) {
        return MAP.get(eClass);
    }
}
