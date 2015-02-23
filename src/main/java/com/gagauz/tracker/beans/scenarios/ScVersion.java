package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.*;
import com.gagauz.tracker.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ScVersion")
public class ScVersion extends DataBaseScenario {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private FeatureVersionDao featureVersionDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private WorkLogDao workLogDao;

    @Autowired
    private TaskCommentDao taskCommentDao;

    @Autowired
    private ScUser scUser;

    @Override
    protected void execute() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        for (int i = 0; i < 1; i++) {
            Project project = new Project();
            project.setKey1("TRACKER");
            project.setName("Трекер (этот проект)");
            CvsRepo repo = new CvsRepo();
            if (true) {
                repo.setType(CvsType.GIT);
                repo.setUrl("https://github.com/gagauz/tracker.git");
                repo.setUsername("gagauz");
                repo.setPassword("p35neog0d");
                repo.setBranch("master");
            } else {
                repo.setType(CvsType.SVN);
                repo.setUrl("file:///R:/projects-my/tracker-svn-repo");
                //                repo.setBranch("trunk");
            }
            project.setCvsRepo(repo);
            projectDao.save(project);

            List<Feature> features = new ArrayList<Feature>();

            for (int h = 0; h < 10; h++) {
                Feature feature = new Feature();
                feature.setProject(project);
                feature.setCreator(user1);
                feature.setName("Название фичи, например, Авторизация.");
                feature.setDescription("Общее описание фичи, например, Авторизация на сайте для доступа к защищенным разделам.");
                features.add(feature);
            }
            featureDao.save(features);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            for (int j = 0; j < 3; j++) {
                Version version = new Version();
                version.setReleased(j == 0);
                version.setProject(project);
                if (j != 4) {
                    version.setName("1." + j);
                    version.setReleaseDate(cal.getTime());
                    cal.add(Calendar.MONTH, 1);
                } else {
                    version.setName("1." + j + "-SNAPSHOT");
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 1);
                    version.setReleaseDate(cal.getTime());
                }

                versionDao.save(version);
                for (Feature feature : features) {
                    if (rand.nextBoolean() && rand.nextBoolean()) {
                        continue;
                    }

                    FeatureVersion featureVersion = new FeatureVersion();
                    featureVersion.setFeature(feature);
                    featureVersion.setVersion(rand.nextBoolean() ? version : null);
                    featureVersion.setOwner(user1);
                    featureVersion.setCreator(user2);
                    featureVersion
                            .setDescription("Название имплементации фичи в данной версии, например, реализовать простую защиту страниц без разделения ролей и форму авторизации с хранением признака авторизованного пользователя в сессии.");

                    featureVersionDao.save(featureVersion);

                    int stc = rand.nextInt(5) + 1;
                    for (int k = 0; k < stc; k++) {
                        Task task = new Task();
                        task.setType(rand.nextBoolean() ? TaskType.TASK : TaskType.BUG);
                        task.setFeatureVersion(featureVersion);
                        task.setOwner(getRandomUser());
                        task.setAuthor(getRandomUser());
                        task.setSummary("Название задачи, например, сделать форму авторизации.");
                        task.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
                        task.setPriority(rand.nextInt(30));
                        int es = 15 * (rand.nextInt(10) + 1);
                        task.setEstimate(es);
                        WorkLog wl = null;
                        if (version.isReleased() || rand.nextBoolean()) {
                            wl = new WorkLog();
                            wl.setTask(task);
                            wl.setUser(task.getAuthor());
                            if (!version.isReleased()) {
                                wl.setLogTime(es / (rand.nextInt(5) + 1));
                                task.setStatus(TaskStatus.IN_PROGRESS);
                            } else {
                                wl.setLogTime(es);
                                task.setStatus(TaskStatus.RESOLVED);
                            }
                        }
                        if (rand.nextBoolean()) {
                            Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                            Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                            task.setAttachments(Arrays.asList(a1, a2));
                        }
                        List<TaskComment> cms = new ArrayList<TaskComment>();
                        if (rand.nextBoolean()) {
                            for (int x = rand.nextInt(10) + 1; x > 0; x--) {
                                TaskComment cm = new TaskComment();
                                cm.setUser(getRandomUser());
                                cm.setTask(task);
                                cm.setText("Lorem ipsum dolorsit.");

                                if (rand.nextBoolean()) {
                                    Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                                    Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                                    cm.setAttachments(Arrays.asList(a1, a2));
                                }
                                cms.add(cm);
                            }
                        }
                        taskDao.save(task);
                        if (wl != null) {
                            workLogDao.save(wl);
                        }
                        taskDao.updateTaskProgessTime(task);
                        if (!cms.isEmpty()) {
                            taskCommentDao.save(cms);
                        }
                    }
                }
            }
        }
    }

    private final Map<Integer, User> userHash = new HashMap<Integer, User>();

    private User getRandomUser() {
        int r = RandomUtils.getRandomInt(9) + 1;
        User u = userHash.get(r);
        if (null == u) {
            u = userDao.findById(r);
            userHash.put(r, u);
        }
        return u;
    }

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] {scUser};
    }
}
