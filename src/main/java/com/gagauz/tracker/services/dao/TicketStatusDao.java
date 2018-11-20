package com.gagauz.tracker.services.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.UserGroup;
import com.xl0e.util.C;

@Service
public class TicketStatusDao extends AbstractDao<Integer, TicketStatus> {

    public List<TicketStatus> findByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

    public List<TicketStatus> findTopLevelByProject(Project project) {
        return getCriteriaFilter().eq("project", project).isNull("parent").list();
    }

    public List<TicketStatus> findFirstByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

    public List<TicketStatus> findCommon() {
        return findByFilter(filter().isNull("project"));
    }

    public List<TicketStatus> findByUserGroupsAndProject(Collection<UserGroup> userGroups, Project project) {
        return getSession().createQuery(
                "select ts from TicketStatus ts join TicketStatusToUserGroup t2g on t2g.id.ticketStatusId=ts.id where t2g.id.userGroupId in (:userGroups) and ts.project=:project")
                .setParameterList("userGroups", C.transform(userGroups, UserGroup::getId))
                .setParameter("project", project)
                .getResultList();
    }

}
