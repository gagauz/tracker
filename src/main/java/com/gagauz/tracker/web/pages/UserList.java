package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class UserList {

    @Property
    @Persist
    private User user;

    @Property
    private User editUser;

    @Inject
    private UserDao userDao;

    void onEdit(Integer id) {
        if (null != id) {
            editUser = userDao.findById(id);
        }
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

    public List<User> getUsers() {
        return userDao.findAll();
    }

}
