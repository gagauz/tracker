package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.services.dao.UserGroupDao;

@Secured
public class RoleGroupList {

    @Property
    private Project project;

    @Property
    private UserGroup roleGroup;

    @Property
    @Persist
    private UserGroup editRoleGroup;

    @Inject
    private UserGroupDao roleGroupDao;

    @Inject
    private Messages messages;

    @Inject
    private SelectModelFactory selectModelFactory;

    void onEdit(Project project, UserGroup roleGroup) {
        this.project = project;
        if (null != roleGroup) {
            this.editRoleGroup = roleGroup;
        }
    }

    void onCreate(Project project) {
        editRoleGroup = new UserGroup();
        editRoleGroup.setProject(project);
    }

    void onReset() {
        editRoleGroup = null;
    }

    void onActivate(Project project, UserGroup roleGroup) {
        onEdit(project, roleGroup);
    }

    void onSuccessFromForm() {
        roleGroupDao.save(editRoleGroup);
    }

    Object onPassivate() {
        return new Object[] { project, editRoleGroup };
    }

    @Cached
    public List<UserGroup> getRoleGroups() {
        if (null != project) {
            return roleGroupDao.findByProject(project);
        }
        return roleGroupDao.findAll();
    }

    @Cached
    public SelectModel getModel() {
        return null;// new EnumSelectModel(AccessRole.class, messages);
    }

    @Cached
    public String getProjectName() {
        return editRoleGroup.getProject() != null ? editRoleGroup.getProject().getName() : messages.get("any");
    }
}
