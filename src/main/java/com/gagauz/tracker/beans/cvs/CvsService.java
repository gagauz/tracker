package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.beans.cvs.git.GitCommitFilter;
import com.gagauz.tracker.beans.cvs.wrapper.GitCvsWrapper;
import com.gagauz.tracker.beans.cvs.wrapper.SvnCvsWrapper;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.CvsType;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CvsService {

    @Autowired
    private ProjectDao projectDao;

    private Map<Project, CvsWrapper> wrapperMap = new HashMap<Project, CvsWrapper>();

    public void init() {
        for (Project project : projectDao.findAll()) {
            initProjectRepo(project);
        }
    }

    public List<Commit> getCommits(Task task) {
        CvsWrapper wrapper = wrapperMap.get(task.getFeature().getProject());
        if (null == wrapper) {
            wrapper = initProjectRepo(task.getFeature().getProject());
        }

        GitCommitFilter filter = new GitCommitFilter();
        filter.setGrep(task.getType().name() + "\\s#" + task.getId());
        return wrapper.getCommits(filter);
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
