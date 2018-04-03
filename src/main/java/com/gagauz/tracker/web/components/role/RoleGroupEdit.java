package com.gagauz.tracker.web.components.role;

import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.xl0e.web.config.Global;

public class RoleGroupEdit {

    @Inject
    private AlertManager alertManager;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Parameter
    @Property
    private RoleGroup object;

    @Property
    private RoleGroup roleGroup;

    public Project getProject() {
        return Global.peek(Project.class);
    }

    boolean setupRender() {
        return getProject() != null;
    }

    public List<RoleGroup> getRoleGroups() {
        return roleGroupDao.findByProject(getProject());
    }

    void onEdit(RoleGroup roleGroup) {
        object = roleGroup;
    }

    void onSuccess() {
        roleGroupDao.save(object);
        alertManager.success("RoleGroup " + object.getName() + " saved");
    }
}
