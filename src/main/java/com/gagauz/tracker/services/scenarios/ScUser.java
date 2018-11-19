package com.gagauz.tracker.services.scenarios;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.services.dao.RoleGroupDao;
import com.gagauz.tracker.services.dao.UserDao;
import com.xl0e.testdata.DataBaseScenario;

@Service("ScUserask")
public class ScUser extends DataBaseScenario {

    private static final String[] NAMES = new String[] { "Name1", "Name2" };

    private static final String[] SURNAMES = new String[] { "Surname1", "Surname2" };

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleGroupDao roleGroupDao;

    @Override
    protected void execute() {

        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setName("Project owner");
        roleGroup.setRoles(new HashSet<>());
        roleGroup.getRoles().add(AccessRole.PROJECT_ADMIN);
        roleGroup.getRoles().add(AccessRole.USER_STORY_CREATOR);
        roleGroup.getRoles().add(AccessRole.TASK_CREATOR);
        roleGroup.getRoles().add(AccessRole.TASK_ASSIGNER);

        roleGroupDao.save(roleGroup);

        RoleGroup roleGroup2 = new RoleGroup();
        roleGroup2.setRoles(new HashSet<>());
        roleGroup2.setName("Quality Assistant");
        roleGroup2.getRoles().add(AccessRole.PROJECT_USER);
        roleGroup2.getRoles().add(AccessRole.BUG_CREATOR);

        roleGroupDao.save(roleGroup2);

        roleGroupDao.flush();

        for (int i = 0; i < 10; i++) {
            User userx = new User();
            userx.setEmail("email" + i + "@email.com");
            userx.setUsername("username" + i);
            boolean b = rand.nextBoolean();
            userx.setName(NAMES[i % 2] + " " + SURNAMES[i % 2]);
            userx.setPassword("111");
            if (null == userx.getRoleGroups()) {
                userx.setRoleGroups(new HashSet<>());
            }
            userx.getRoleGroups().add(roleGroup);

            userDao.save(userx);
        }
        userDao.flush();
    }
}
