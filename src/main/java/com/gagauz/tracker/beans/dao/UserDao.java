package com.gagauz.tracker.beans.dao;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.User;

@Service
public class UserDao extends AbstractDao<Integer, User> {

    public User findByToken(String token) {
        return null;
    }

    public User findByUsername(String username) {
        return (User) getSession().createQuery("from User u where username=:username").setString("username", username).uniqueResult();
    }

}
