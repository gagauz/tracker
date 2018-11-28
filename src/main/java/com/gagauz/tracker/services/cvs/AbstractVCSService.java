package com.gagauz.tracker.services.cvs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.cvs.Branch;
import com.gagauz.tracker.services.dao.cvs.BranchDao;
import com.gagauz.tracker.services.dao.cvs.RepositoryDao;
import com.xl0e.hibernate.utils.EntityFilterBuilder;
import com.xl0e.util.C;

public abstract class AbstractVCSService implements CvsRepositoryService {
    private static Logger LOG;
    static {
        LOG = LoggerFactory.getLogger(AbstractVCSService.class);
    }

    private static final ResponseErrorHandler DEFAULT_HANDLER = new ResponseErrorHandler() {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            LOG.error("Http error {} : {}", response.getRawStatusCode(), response.getStatusText());
        }
    };

    @Inject
    protected BranchDao branchDao;

    @Inject
    protected RepositoryDao repositoryDao;

    @Override
    public boolean hasBranches(Version version) {
        return branchDao.countByFilter(EntityFilterBuilder.eq("version", version)) > 0;
    }

    @Override
    public List<Branch> getBranches(Version version) {
        return branchDao.findByVersion(version);
    }

    protected static RestTemplate createTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(DEFAULT_HANDLER);

        Set<HttpMessageConverter<?>> converters = C.hashSet(restTemplate.getMessageConverters());
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new FormHttpMessageConverter());

        restTemplate.setMessageConverters(C.arrayList(converters));

        return restTemplate;
    }

    protected static URI uri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
