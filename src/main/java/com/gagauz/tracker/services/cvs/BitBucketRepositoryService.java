package com.gagauz.tracker.services.cvs;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;
import com.gagauz.tracker.db.model.cvs.ProjectRepository;
import com.gagauz.tracker.db.model.cvs.Repository;
import com.gagauz.tracker.utils.JsonUtils;
import com.xl0e.util.C;

//@Service
public class BitBucketRepositoryService extends AbstractVCSService {
    private static final Logger LOG = LoggerFactory.getLogger(BitBucketRepositoryService.class);

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
        if (C.isEmpty(versions)) {
            return;
        }

        ProjectRepository projectRepository = versions.get(0).getProject().getProjectRepository();
        Repository repository = projectRepository.getRepository();

        ResponseEntity<HashMap> branches = createTemplate().getForEntity(
                uri("https://api.bitbucket.org/2.0/repositories/" + repository.getUsername() + "/" + projectRepository.getName() + "/refs/branches"),
                HashMap.class);

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
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

    @SuppressWarnings({ "rawtypes" })
    private boolean authenticate(Repository repo) {

        ResponseEntity<HashMap> response = createTemplate().getForEntity(
                "https://bitbucket.org/site/oauth2/authorize",
                HashMap.class,
                "client_id",
                repo.getOauth().getClientID(),
                "response_type",
                "code");

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

    public static void main(String[] args) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String plainCreds = "1:1";
        byte[] plainCredsBytes = plainCreds.getBytes();
        plainCredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        String base64Creds = new String(plainCredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<Map> request = new HttpEntity<>(map, headers);

        ResponseEntity<HashMap> branches = createTemplate().exchange(
                "https://api.bitbucket.org/2.0/repositories/xl0e/ivaga-shop/refs/branches",
                HttpMethod.GET,
                request,
                HashMap.class);

        //        ResponseEntity<HashMap> branches = createTemplate().exchange(
        //                "https://bitbucket.org/site/oauth2/access_token",
        //                HttpMethod.POST,
        //                request,
        //                HashMap.class);

        System.out.println(branches.getHeaders());
        System.out.println(branches.getBody());
    }
}
