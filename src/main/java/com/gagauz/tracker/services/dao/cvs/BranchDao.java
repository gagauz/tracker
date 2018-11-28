package com.gagauz.tracker.services.dao.cvs;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;
import com.gagauz.tracker.services.dao.AbstractDao;

@Service
public class BranchDao extends AbstractDao<Integer, Branch> {

	public List<Branch> findByVersion(Version version) {
		return getCriteriaFilter().eq("version", version).list();
	}

}
