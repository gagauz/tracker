package com.gagauz.tracker.services.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagauz.tracker.services.cvs.CvsRepositoryService;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.gagauz.tracker.services.dao.cvs.BranchDao;

@Service
public class SchedulerService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private CvsRepositoryService cvsRepositoryService;

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    @Transactional
    public void execute() {

        try {

            cvsRepositoryService.connectVersionWithVCS(versionDao.findUnconnectedVersions());

        } catch (Exception e) {
            //TODO: Record error
            e.printStackTrace();
        }
    }

}
