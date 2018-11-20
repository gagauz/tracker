package com.gagauz.tracker.web.components.role;

import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.services.dao.UserGroupDao;

import org.apache.tapestry5.web.config.Global;

public class RoleGroupEdit {

    @Inject
    private AlertManager alertManager;

    @Inject
    private UserGroupDao roleGroupDao;

    @Parameter
    @Property
    private UserGroup object;

    @Property
    private UserGroup roleGroup;

    public Project getProject() {
        return Global.peek(Project.class);
    }

    boolean setupRender() {
        return getProject() != null;
    }

    public List<UserGroup> getRoleGroups() {
        return roleGroupDao.findByProject(getProject());
    }

    void onEdit(UserGroup roleGroup) {
        object = roleGroup;
    }

    void onSuccess() {
        roleGroupDao.save(object);
        alertManager.success("UserGroup " + object.getName() + " saved");
    }
}
