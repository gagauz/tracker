package com.gagauz.tracker.beans.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskType;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

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

            for (int h = 0; h < 10; h++) {
                Feature th = new Feature();
                th.setProject(p);
                th.setCreator(user1);
                th.setName("Feature #" + h);
                featureDao.save(th);
                theaders.add(th);
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -4);
            for (int j = 0; j < 5; j++) {
                Version v = new Version();
                v.setProject(p);
                if (j != 4) {
                    v.setReleased(true);
                    v.setVersion("1." + j);
                    v.setReleaseDate(cal.getTime());
                    cal.add(Calendar.MONTH, 1);
                } else {
                    v.setReleased(false);
                    v.setVersion("1." + j + "-SNAPSHOT");
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 1);
                    v.setReleaseDate(cal.getTime());
                }

                versionDao.save(v);
                for (Feature th : theaders) {
                    if (rand.nextBoolean() && rand.nextBoolean()) {
                        continue;
                    }
                    FeatureVersion t = new FeatureVersion();
                    t.setFeature(th);
                    t.setVersion(v);
                    t.setOwner(user1);
                    t.setCreator(user2);
                    t.setName("#" + th.getId() + "/" + v.getVersion());
                    t.setDescription("Lorem ipsum — dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.");
                    Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                    Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                    t.setAttachments(Arrays.asList(a1, a2));
                    featureVersionDao.save(t);

                    int stc = rand.nextInt(5) + 1;
                    for (int k = 0; k < stc; k++) {
                        Task st = new Task();
                        st.setType(TaskType.TASK);
                        //                        st.setFeatureVersion(t);
                        st.setFeature(t.getFeature());
                        st.setVersion(t.getVersion());
                        st.setOwner(user1);
                        st.setCreator(user2);
                        st.setSummary("Task #" + k);
                        st.setDescription("Lorem ipsum dolorsit.");
                        if (rand.nextBoolean()) {
                            int es = 15 * (rand.nextInt(10) + 1);
                            st.setEstimated(es);
                            st.setProgress(es / (rand.nextInt(5) + 1));
                        }
                        taskDao.save(st);
                    }

                    stc = rand.nextInt(3);
                    for (int k = 0; k < stc; k++) {
                        Task st = new Task();
                        st.setType(TaskType.BUG);
                        //                        st.setFeatureVersion(t);
                        st.setFeature(t.getFeature());
                        st.setVersion(t.getVersion());
                        st.setOwner(user2);
                        st.setCreator(user1);
                        st.setSummary("Bug #" + k);
                        st.setDescription("Lorem ipsum dolorsit.");
                        if (rand.nextBoolean()) {
                            int es = 15 * (rand.nextInt(10) + 1);
                            st.setEstimated(es);
                            st.setProgress(es / (rand.nextInt(5) + 1));
                        }
                        taskDao.save(st);
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
