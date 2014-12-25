package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private TaskHeaderDao taskHeaderDao;

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
            p.setName("PROJECT");
            projectDao.save(p);

            List<TaskHeader> theaders = new ArrayList<TaskHeader>();

            for (int h = 0; h < 10; h++) {
                TaskHeader th = new TaskHeader();
                th.setProject(p);
                th.setCreator(user1);
                th.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
                th.setName("TASK HEADER #" + h);
                taskHeaderDao.save(th);
                theaders.add(th);
            }
            for (int j = 0; j < 2; j++) {
                Version v = new Version();
                v.setProject(p);
                v.setVersion("1." + j + "-SNAPSHOT");
                versionDao.save(v);
                for (TaskHeader th : theaders) {
                    Task t = new Task();
                    t.setTaskHeader(th);
                    t.setVersion(v);
                    t.setOwner(user1);
                    t.setCreator(user2);
                    t.setName("Task of taskheader " + th.getName() + " version " + v.getVersion());
                    t.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                            "Pellentesque malesuada scelerisque arcu, non laoreet ligula semper eget. " +
                            "Fusce fringilla orci tellus, maximus sagittis lacus viverra sit amet. " +
                            "Maecenas id imperdiet velit, ac dictum orci. " +
                            "Nullam porttitor quam nisl, ut sagittis tortor faucibus quis. " +
                            "Ut vel diam semper, congue purus sed, cursus lacus. " +
                            "Aliquam vulputate lorem non sapien fringilla, id efficitur est posuere. " +
                            "Nam eleifend, dolor at vehicula dignissim, risus nibh bibendum eros, vel commodo tellus justo at nulla. Nulla a lacinia est. "
                            +
                            "Mauris at lacus mollis, viverra augue ac, laoreet nibh.");
                    taskDao.save(t);

                    for (int k = 0; k < 10; k++) {
                        SubTask st = new SubTask();
                        st.setTask(t);
                        st.setOwner(user1);
                        st.setCreator(user2);
                        st.setSummary("SubTask #" + k);
                        st.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Pellentesque malesuada scelerisque arcu, non laoreet ligula semper eget. " +
                                "Fusce fringilla orci tellus, maximus sagittis lacus viverra sit amet. " +
                                "Maecenas id imperdiet velit, ac dictum orci. " +
                                "Nullam porttitor quam nisl, ut sagittis tortor faucibus quis. " +
                                "Ut vel diam semper, congue purus sed, cursus lacus. " +
                                "Aliquam vulputate lorem non sapien fringilla, id efficitur est posuere. " +
                                "Nam eleifend, dolor at vehicula dignissim, risus nibh bibendum eros, vel commodo tellus justo at nulla. Nulla a lacinia est. "
                                +
                                "Mauris at lacus mollis, viverra augue ac, laoreet nibh.");
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
