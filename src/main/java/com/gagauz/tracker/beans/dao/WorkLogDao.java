package com.gagauz.tracker.beans.dao;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.WorkLog;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class WorkLogDao extends AbstractHibernateDao<Integer, WorkLog> {

}
