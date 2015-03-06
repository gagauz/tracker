package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserDao extends AbstractDao<Integer, User> {

    public User findByToken(String token) {
        return (User) getSession().createQuery("from User u where token=:token").setString("token", token).uniqueResult();
    }

    public User findByUsername(String username) {
        return (User) getSession().createQuery("from User u where username=:username").setString("username", username).uniqueResult();
    }

}
