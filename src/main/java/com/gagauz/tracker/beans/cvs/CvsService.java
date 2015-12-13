package com.gagauz.tracker.beans.cvs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.cvs.wrapper.GitCvsWrapper;
import com.gagauz.tracker.beans.cvs.wrapper.SvnCvsWrapper;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.CvsType;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;

@Service
public class CvsService {

    @Autowired
    private ProjectDao projectDao;

    private Map<Project, CvsWrapper> wrapperMap = new HashMap<>();

    public void init() {
        for (Project project : projectDao.findAll()) {
            initProjectRepo(project);
        }
    }

    public List<Commit> getCommits(Ticket ticket) {
        CvsWrapper wrapper = wrapperMap.get(ticket.getFeature().getProject());
        if (null == wrapper) {
            wrapper = initProjectRepo(ticket.getFeature().getProject());
        }

        return wrapper.getCommits(ticket);
    }

    private CvsWrapper initProjectRepo(Project project) {
        try {
            CvsWrapper wrapper = null;
            if (project.getCvsRepo().getType() == CvsType.GIT) {
                wrapper = new GitCvsWrapper();
            } else if (project.getCvsRepo().getType() == CvsType.SVN) {
                wrapper = new SvnCvsWrapper();
            } else {
                throw new RuntimeException("Unimplemented CVS type " + project.getCvsRepo().getType());
            }
            wrapper.init(project);
            wrapperMap.put(project, wrapper);
            return wrapper;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
