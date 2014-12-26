package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private TaskDao taskDao;

    @Autowired
    private SubTaskDao subTaskDao;

    @Autowired
    private BugDao bugDao;

    @Autowired
    private ScUser scUser;

    @Override
    protected void execute() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        for (int i = 0; i < 1; i++) {
            Project p = new Project();
            p.setName("Трекер (этот проект)");
            projectDao.save(p);

            List<Feature> theaders = new ArrayList<Feature>();

            for (int h = 0; h < 20; h++) {
                Feature th = new Feature();
                th.setProject(p);
                th.setCreator(user1);
                th.setName("Feature #" + h);
                featureDao.save(th);
                theaders.add(th);
            }
            for (int j = 0; j < 5; j++) {
                Version v = new Version();
                v.setProject(p);
                v.setVersion("1." + j + (j == 4 ? "-SNAPSHOT" : ""));
                versionDao.save(v);
                for (Feature th : theaders) {
                    Task t = new Task();
                    t.setFeature(th);
                    t.setVersion(v);
                    t.setOwner(user1);
                    t.setCreator(user2);
                    t.setName("Release task on " + v.getVersion());
                    t.setDescription("Вывод всех проектов в таблице. Колонки ...");
                    Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                    Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                    t.setAttachments(Arrays.asList(a1, a2));
                    taskDao.save(t);

                    int stc = rand.nextInt(5) + 1;
                    for (int k = 0; k < stc; k++) {
                        SubTask st = new SubTask();
                        st.setTask(t);
                        st.setOwner(user1);
                        st.setCreator(user2);
                        st.setSummary("Subtask #" + k);
                        st.setDescription("Lorem ipsum dolorsit.");
                        subTaskDao.save(st);
                    }
                }
            }
        }
    }

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] {scUser};
    }
}
