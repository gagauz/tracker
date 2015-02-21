package com.gagauz.tracker.beans.cvs;

import com.gagauz.tracker.beans.cvs.git.GitCommitFilter;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Commit;
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

    private Map<Project, CvsWrapper> dotGitMap = new HashMap<Project, CvsWrapper>();

    public void init() {
        for (Project project : projectDao.findAll()) {
            initProjectRepo(project);
        }
    }

    public List<Commit> getCommits(Task task) {
        CvsWrapper dotGit = dotGitMap.get(task.getFeature().getProject());
        if (null == dotGit) {
            dotGit = initProjectRepo(task.getFeature().getProject());
        }

        GitCommitFilter filter = new GitCommitFilter();
        filter.setGrep(task.getType().name() + "\\s#" + task.getId());
        return dotGit.getCommits(filter);
    }

    private CvsWrapper initProjectRepo(Project project) {
        try {
            CvsWrapper dotGit = new GitCvsWrapper();
            dotGit.init(project);
            dotGitMap.put(project, dotGit);
            return dotGit;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
