package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.User;
import com.xl0e.hibernate.dao.AbstractDao;

@Service
public class UserDao extends AbstractDao<Integer, User> {

    public User findByToken(String token) {
        return findOneByFilter(filter().eq("token", token));
    }

    public User findByUsername(String username) {
        return (User) getSession().createQuery("from User u where username=:username").setString("username", username).uniqueResult();
    }

    public List<User> findByNameOrEmail(String username) {
        return null;
    }

}
