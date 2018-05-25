package com.gagauz.tracker.services.scenarios;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.services.dao.RoleGroupDao;
import com.gagauz.tracker.services.dao.UserDao;
import com.gagauz.tracker.utils.RandomUtils;
import com.xl0e.testdata.DataBaseScenario;

@Service("ScUserask")
public class ScUser extends DataBaseScenario {

    private static final String[][] NAMES = new String[][] {
            { "Исаак", "Авраам", "Аарон", "Иоаким", "Наум", "Иосиф", "Бен", "Моисей", "Исаия",
                    "Ной", "Натаниэль", "Самуил", "Иоганн" },
            { "Жанна", "Лия", "Мария", "Мирра", "Наоми", "Суламифь", "Ребекка", "Ноа", "Сара",
                    "Магдалина", "Динора", "Анна", "Яна" } };

    private static final String[][] PATRONIMIC = new String[][] {
            { "Исаакович", "Абрамович", "Ааронович", "Иоакимович", "Наумович", "Иосифович",
                    "Бенович", "Моисеевич", "Исаиевич", "Ноиевич", "Натаниэлевич", "Самуилович",
                    "Иоганнович" },
            { "Исааковна", "Абрамовна", "Аароновна", "Иоакимовна", "Наумовна", "Иосифовна",
                    "Беновна", "Моисеевна", "Исаиевна", "Ноиевна", "Натаниэлевна", "Самуиловна",
                    "Иоганновна" } };

    private static final String[] SURNAMES = new String[] { "Бромберг", "Вольфензон", "Лангман",
            "Кларсфельд", "Рамон", "Познер", "Ойербах", "Перельман", "Фельдман", "Гольдман",
            "Герштейн", "Гофман", "Рабинович" };

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

        for (int i = 0; i < 50; i++) {
            User userx = new User();
            userx.setEmail("email" + i + "@email.com");
            userx.setUsername("username" + i);
            boolean b = rand.nextBoolean();
            userx.setName(getRandomName(b) + " " + getRandomSurname());
            userx.setPassword("111");
            if (null == userx.getRoleGroups()) {
                userx.setRoleGroups(new HashSet<>());
            }
            userx.getRoleGroups().add(roleGroup);

            userDao.save(userx);
        }
        userDao.flush();
    }

    protected String getRandomName(boolean male) {
        return RandomUtils.getRandomObject(NAMES[male ? 0 : 1]);
    }

    protected String getRandomPatr(boolean male) {
        return RandomUtils.getRandomObject(PATRONIMIC[male ? 0 : 1]);
    }

    protected String getRandomSurname() {
        return RandomUtils.getRandomObject(SURNAMES);
    }

}
