package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import java.util.List;

public class UserList {

    @Property
    private User user;

    @Property
    @Persist
    private User editUser;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @Inject
    private UserDao userDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Inject
    private ComponentResources resources;

    @Inject
    private SelectModelFactory selectModelFactory;

    void onEdit(Integer id) {
        if (null != id && id > 0) {
            editUser = userDao.findById(id);
        }
    }

    void onCreate() {
        editUser = new User();
    }

    void onReset() {
        editUser = null;
    }

    void onActivate(Integer id) {
        onEdit(id);
    }

    void onSuccessFromForm() {
        userDao.save(editUser);
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

    @Cached
    public ValueEncoder<RoleGroup> getEncoder() {
        return valueEncoderSource.getValueEncoder(RoleGroup.class);
    }

}
