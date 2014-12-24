package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScUserask")
public class ScUser extends DataBaseScenario {

    @Autowired
    private UserDao userDao;

    @Override
    protected void execute() {
        User user = new User();
        user.setEmail("user1@email.com");
        user.setName("user1");
        user.setPassword("111");

        userDao.save(user);

        user = new User();
        user.setEmail("user2@email.com");
        user.setName("user2");
        user.setPassword("111");

        userDao.save(user);
    }

}
