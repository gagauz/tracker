package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.RoleGroup;
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
        roleGroup.getRoles().add(Role.PROJECT_ADMIN);
        roleGroup.getRoles().add(Role.USER_STORY_CREATOR);
        roleGroup.getRoles().add(Role.TASK_CREATOR);
        roleGroup.getRoles().add(Role.TASK_ASSIGNER);

        roleGroupDao.save(roleGroup);

        RoleGroup roleGroup2 = new RoleGroup();
        roleGroup2.setName("Quality Assistant");
        roleGroup2.getRoles().add(Role.PROJECT_USER);
        roleGroup2.getRoles().add(Role.BUG_CREATOR);

        roleGroupDao.save(roleGroup2);

        User user = new User();
        user.setEmail("user1@email.com");
        user.setName("user1");
        user.getRoleGroups().add(roleGroup);
        user.getRoleGroups().add(roleGroup2);
        user.setPassword("111");

        userDao.save(user);

        User user1 = new User();
        user1.setEmail("user2@email.com");
        user1.setName("user2");
        user1.setPassword("111");
        user1.getRoleGroups().add(roleGroup2);

        userDao.save(user1);
    }

}
