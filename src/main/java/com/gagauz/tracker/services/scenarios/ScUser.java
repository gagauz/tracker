package com.gagauz.tracker.services.scenarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.services.dao.UserDao;
import com.xl0e.testdata.DataBaseScenario;

@Service("ScUserask")
public class ScUser extends DataBaseScenario {

    private static final String[] NAMES = { "Name1", "Name2" };

    private static final String[] SURNAMES = { "Surname1", "Surname2" };

    @Autowired
    private UserDao userDao;

    @Override
    protected void execute() {
        for (int i = 0; i < 10; i++) {
            User userx = new User();
            userx.setEmail("email" + i + "@email.com");
            userx.setUsername("username" + i);
            boolean b = rand.nextBoolean();
            userx.setName(NAMES[i % 2] + " " + SURNAMES[i % 2]);
            userx.setPasswordRaw("111");
            userDao.save(userx);
        }
        userDao.flush();
    }

}
