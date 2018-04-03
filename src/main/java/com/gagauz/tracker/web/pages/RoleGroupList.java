package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.AccessRole;
import org.apache.tapestry5.web.services.security.Secured;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.util.EnumSelectModel;

import java.util.List;

@Secured
public class RoleGroupList {

    @Property
    private Project project;

    @Property
    private RoleGroup roleGroup;

    @Property
    @Persist
    private RoleGroup editRoleGroup;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Inject
    private Messages messages;

    @Inject
    private SelectModelFactory selectModelFactory;

    void onEdit(Project project, RoleGroup roleGroup) {
        this.project = project;
        if (null != roleGroup) {
            this.editRoleGroup = roleGroup;
        }
    }

    void onCreate(Project project) {
        editRoleGroup = new RoleGroup();
        editRoleGroup.setProject(project);
    }

    void onReset() {
        editRoleGroup = null;
    }

    void onActivate(Project project, RoleGroup roleGroup) {
        onEdit(project, roleGroup);
    }

    void onSuccessFromForm() {
        roleGroupDao.save(editRoleGroup);
    }

    Object onPassivate() {
        return new Object[] {project, editRoleGroup};
    }

    @Cached
    public List<RoleGroup> getRoleGroups() {
        if (null != project) {
            return roleGroupDao.findByProject(project);
        }
        return roleGroupDao.findAll();
    }

    @Cached
    public SelectModel getModel() {
        return new EnumSelectModel(AccessRole.class, messages);
    }

    @Cached
    public String getProjectName() {
        return editRoleGroup.getProject() != null ? editRoleGroup.getProject().getName() : messages.get("any");
    }
}
