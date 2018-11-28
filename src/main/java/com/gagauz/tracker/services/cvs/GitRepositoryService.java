package com.gagauz.tracker.services.cvs;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;
import com.gagauz.tracker.db.model.cvs.Repository;
import com.gagauz.tracker.services.dao.cvs.BranchDao;
import com.gagauz.tracker.services.dao.cvs.RepositoryDao;
import com.gagauz.tracker.utils.JsonUtils;
import com.xl0e.hibernate.utils.EntityFilterBuilder;

@Service
public class GitRepositoryService extends AbstractVCSService {
    private static final Logger LOG = LoggerFactory.getLogger(GitRepositoryService.class);

    @Inject
    private BranchDao branchDao;

    @Inject
    private RepositoryDao repositoryDao;

    @Override
    public boolean hasBranches(Version version) {
        return branchDao.countByFilter(EntityFilterBuilder.eq("version", version)) > 0;
    }

    @Override
    public List<Branch> getBranches(Version version) {
        return branchDao.findByVersion(version);
    }

    @Override
    public void createBranch(Branch branch) {
        // TODO create branch in remote repo
        branchDao.save(branch);
    }

    @Override
    public void deleteBranch(Branch branch) {
        // TODO remove branch in remote repo
        branchDao.delete(branch);
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public void connectVersionWithVCS(List<Version> versions) {
        //        ProjectRepository projectRepo = version.getProject().getProjectRepository();
        //
        //        if (!checkAuth(projectRepo.getRepository())) {
        //            return;
        //        }
        //
        //        List<Branch> branches = branchDao.findByVersion(version);
        //
        //        if (branches.isEmpty()) {
        //
        //            Branch defBranch = getDefaultBranch(version);
        //
        //            String url = "https://api.github.com/repos/" + projectRepo.getRepository().getName() + "/" + projectRepo.getName() + "/branches/" + defBranch.getName();
        //
        //            Map<String, String> headers = new HashMap<>();
        //            headers.put("Authorization", "token " + projectRepo.getRepository().getOauth().getToken());
        //
        //            RequestEntity request = new RequestEntity<>(headers, HttpMethod.GET, uri(url));
        //
        //            ResponseEntity<HashMap> response = createTemplate().exchange(request, HashMap.class);
        //
        //            System.out.println(response.getBody());
        //
        //            if (response.getStatusCodeValue() == 200) {
        //
        //            }
        //        }
    }

    @SuppressWarnings({ "rawtypes" })
    private boolean checkAuth(Repository repository) {

        if (null == repository.getOauth().getClientID() || null == repository.getOauth().getClientSecret()) {
            return false;
        }

        if (null != repository.getOauth().getToken()) {
            return true;
        }

        RequestEntity request = new RequestEntity<>(HttpMethod.GET,
                uri("https://api.github.com/authorizations/"
                        + repository.getOauth().getClientID()
                        + "/tokens/"
                        + repository.getOauth().getClientID()));

        ResponseEntity<HashMap> response = createTemplate().exchange(request, HashMap.class);

        if (response.getStatusCodeValue() / 100 != 2) {
            return authenticate(repository);
        }
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean authenticate(Repository repo) {

        Map body = new HashMap<>();

        body.put("client_secret", repo.getOauth().getClientSecret());
        body.put("scopes", Arrays.asList("public_repo"));
        body.put("note", "admin script");

        RequestEntity request = new RequestEntity<>(body, HttpMethod.PUT, uri("https://api.github.com/authorizations/clients/" + repo.getOauth().getClientID()));

        ResponseEntity<HashMap> response = createTemplate().exchange(request, HashMap.class);

        repo.getOauth().setToken(null);

        if (response.getStatusCodeValue() / 100 == 2) {
            JsonUtils.parseString(response.getBody(), "token").ifPresent(token -> {
                repo.getOauth().setToken(token);
                repo.getOauth().setCreationTime(new Date());
                repositoryDao.save(repo);
            });
        }
        return repo.getOauth().getToken() != null;

    }

}
