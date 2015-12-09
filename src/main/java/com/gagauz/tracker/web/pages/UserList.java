package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.security.Secured;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import java.util.List;

@Secured
public class UserList {

    @Property
    private User user;

    @Property
    @Persist
    private User editUser;

    @Inject
    private UserDao userDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Inject
    private SelectModelFactory selectModelFactory;

    private boolean canceled;

    void onEdit(Integer id) {
        if (null != id && id > 0) {
            editUser = userDao.findById(id);
        }
    }

    void onCreate() {
        editUser = new User();
    }

    void onActivate(Integer id) {
        onEdit(id);
    }

    void onSuccessFromForm() {
        if (null != editUser && !canceled)
            userDao.save(editUser);

        if (canceled) {
            editUser = null;
        }
    }

    void onCanceledFromForm() {
        editUser = null;
        canceled = true;
    }

    Object onPassivate() {
        return editUser != null ? editUser.getId() : null;
    }

    @Cached
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Cached
    public SelectModel getAllRoleGroups() {
        return selectModelFactory.create(roleGroupDao.findAll(), "name");
    }

}
