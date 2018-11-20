package com.gagauz.tracker.services.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.UserGroup;
import com.xl0e.util.C;

@Service
public class TicketTypeDao extends AbstractDao<Integer, TicketType> {

    public List<TicketType> findByProject(Project project) {
        return getCriteriaFilter().or().isNull("project").eq("project", project).list();
    }

    public List<TicketType> findCommon() {
        return getCriteriaFilter().or().isNull("project").list();
    }

    public List<TicketType> findByUserGroupsAndProject(Collection<UserGroup> userGroups, Project project) {
        return getSession().createQuery(
                "select distinct tt from TicketType tt join TicketTypeToUserGroup t2g on t2g.id.ticketTypeId=tt.id where t2g.id.userGroupId in (:userGroups) and tt.project=:project")
                .setParameterList("userGroups", C.transform(userGroups, UserGroup::getId))
                .setParameter("project", project)
                .getResultList();
    }

}
