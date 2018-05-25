package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.User;

@Service
public class UserDao extends AbstractDao<Integer, User> {

    public User findByToken(String token) {
        return filter().eq("token", token).uniqueResult();
    }

    public User findByUsername(String username) {
        return filter().eq("username", username).uniqueResult();
    }

    public User findByNameAndPass(String username, String password) {
        return filter().eq("username", username).eq("password", password).uniqueResult();
    }

    public List<User> findSimilarByNameOrUsername(String username) {
        return filter().like("username", username + "%").list();
    }

}
