package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.CanbanGroup;
import com.gagauz.tracker.db.model.Project;

@Service
public class CanbanGroupDao extends AbstractDao<Integer, CanbanGroup> {

	public List<CanbanGroup> findByProject(Project project) {
		return getCriteriaFilter().eq("project", project).orderAsc("sort").list();
	}

}
