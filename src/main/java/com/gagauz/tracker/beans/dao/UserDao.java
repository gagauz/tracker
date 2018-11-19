package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.User;

@Service
public class UserDao extends AbstractDao<Integer, User> {

    public User findByToken(String token) {
        return getCriteriaFilter()
                .eq("token", token)
                .uniqueResult();
    }

    public User findByUsername(String username) {
        return getCriteriaFilter()
                .eq("username", username)
                .uniqueResult();
    }

    public User findByUsernameAndPassword(String username, String password) {
        return getCriteriaFilter()
                .eq("username", username)
                .eq("password", password)
                .uniqueResult();
    }

    public List<User> findByNameOrEmail(String username) {
        return null;
    }

}
