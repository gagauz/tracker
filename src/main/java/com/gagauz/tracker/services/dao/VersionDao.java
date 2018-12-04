package com.gagauz.tracker.services.dao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;

@Service
public class VersionDao extends AbstractDao<Integer, Version> {

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project) {
        return getSession().createQuery("from Version v where project=:project").setEntity("project", project).list();
    }

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project, boolean released) {
        return getCriteriaFilter().eq("project", project).eq("released", released).list();
    }

    public Version findLast(Project project) {
        List<Version> list = getSession().createQuery("from Version v where project=:project order by name desc").setMaxResults(1)
                .setEntity("project", project)
                .list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<Version> findByFeature(Feature feature) {
        return getSession().createQuery("select distinct t.version from Ticket t "
                + "where t.feature=:feature").setParameter("feature", feature).list();
    }

    public Version findByName(String name) {
        return getCriteriaFilter().eq("name", name).uniqueResult();
    }

    public List<Version> findUnconnectedVersions() {
        Date date = Date.from(LocalDateTime.now().minusHours(1).toInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())));
        return getSession().createQuery("select v from Version v "
                + "join Project p on p.id=v.project.id "
                + "join ProjectRepository pr on pr.project.id=p.id "
                + "join Repository r on r.id=pr.repository.id "
                + "join Branch b on b.version.id=v.id "
                + "where r is not null AND (b is null or b.lastCommitDate is null or b.lastCommitDate < :date)").list();
    }
}
