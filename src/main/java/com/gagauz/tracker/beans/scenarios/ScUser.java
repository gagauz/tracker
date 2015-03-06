package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Roles;
import com.gagauz.tracker.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScUserask")
public class ScUser extends DataBaseScenario {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleGroupDao roleGroupDao;

    @Override
    protected void execute() {

        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setName("Project owner");
        roleGroup.getRoles().add(Roles.PROJECT_ADMIN);
        roleGroup.getRoles().add(Roles.USER_STORY_CREATOR);
        roleGroup.getRoles().add(Roles.TASK_CREATOR);
        roleGroup.getRoles().add(Roles.TASK_ASSIGNER);

        roleGroupDao.save(roleGroup);

        RoleGroup roleGroup2 = new RoleGroup();
        roleGroup2.setName("Quality Assistant");
        roleGroup2.getRoles().add(Roles.PROJECT_USER);
        roleGroup2.getRoles().add(Roles.BUG_CREATOR);

        roleGroupDao.save(roleGroup2);

        roleGroupDao.flush();

        User user = new User();
        user.setEmail("user1@email.com");
        user.setName("Dev Null");
        user.setUsername("user1");
        user.getRoleGroups().add(roleGroup);
        user.getRoleGroups().add(roleGroup2);
        user.setPassword("111");

        userDao.save(user);

        User user1 = new User();
        user1.setEmail("user2@email.com");
        user1.setUsername("Api Doc");
        user1.setName("user2");
        user1.setPassword("111");
        user1.getRoleGroups().add(roleGroup2);

        userDao.save(user1);

        for (int i = 0; i < 10; i++) {
            User userx = new User();
            userx.setEmail("email" + i + "@email.com");
            userx.setUsername("username" + i);
            userx.setName("User " + i + " Surname");
            userx.setPassword("111");
            userx.getRoleGroups().add(roleGroup2);

            userDao.save(userx);
        }
        userDao.flush();
    }

}
