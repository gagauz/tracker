package com.gagauz.tracker.services.dao.cvs;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.cvs.Repository;
import com.gagauz.tracker.services.dao.AbstractDao;

@Service
public class RepositoryDao extends AbstractDao<Integer, Repository> {

	public Repository findByProject(Project project) {
		return getCriteriaFilter().eq("project", project).uniqueResult();
	}

}
