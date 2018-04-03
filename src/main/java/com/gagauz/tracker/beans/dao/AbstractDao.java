package com.gagauz.tracker.beans.dao;

import java.io.Serializable;

import com.xl0e.hibernate.dao.AbstractHibernateDao;
import com.xl0e.hibernate.model.IModel;

public class AbstractDao<Id extends Serializable, E extends IModel<Id>> extends AbstractHibernateDao<Id, E> {

}
