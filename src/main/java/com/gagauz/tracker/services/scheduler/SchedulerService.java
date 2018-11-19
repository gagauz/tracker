package com.gagauz.tracker.services.scheduler;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.ProjectDao;

@Service
public class SchedulerService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private SessionFactory sessionFactory;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @Transactional
    public void update() {
        updateNonTransactional();
    }

    public void updateNonTransactional() {
        executor.shutdownNow();
        List<Project> schedulers = projectDao.findAll();
        executor = Executors.newScheduledThreadPool(schedulers.size());
        for (final Project scheduler : schedulers) {
            executor.execute(createRunnable(scheduler.getId(), new CronTrigger("*/1 * * ? * ?")));
        }
    }

    protected Runnable createRunnable(final int schedulerId, final CronTrigger cronTrigger) {
        return new Runnable() {

            Date actualExecTime;
            Date scheduledExecTime;
            Date completionTime;

            @Override
            public void run() {
                actualExecTime = new Date();
                try {
                    //Skip first execution
                    if (scheduledExecTime != null) {
                        System.out.println("Execute [" + schedulerId + "]");
                        //                        StageTrigger trigger = stageTriggerDao.findById(schedulerId);
                        //                        System.out.println("Execute trigger " + trigger.getData());
                        //                        if (trigger.getType() == Type.SCRIPT) {
                        //                            StringBuilder sb = new StringBuilder();
                        //
                        //                            String projectDir = PathUtils.getProjectBaseDir(trigger.getParent().getProject());
                        //                            File dir = new File(projectDir);
                        //
                        //                            BashUtils.execute(dir, sb, trigger.getData());
                        //                            System.out.println("--------------------------------------------------------------------");
                        //                            System.out.println(sb.toString());
                        //                            System.out.println("--------------------------------------------------------------------");
                        //                        }
                    }
                    Thread.sleep(15000);
                } catch (Exception e) {
                    //TODO: Record error
                    e.printStackTrace();
                } finally {
                    // Schedule next execution
                    completionTime = new Date();

                    System.out.println("actualExecTime    " + actualExecTime);
                    System.out.println("completionTime    " + completionTime);
                    System.out.println("scheduledExecTime " + scheduledExecTime);

                    scheduledExecTime = cronTrigger.nextExecutionTime(getContext());
                    long delay = scheduledExecTime.getTime() - System.currentTimeMillis();
                    System.out.println("Next exec time [" + scheduledExecTime + "]");
                    executor.schedule(this, delay, TimeUnit.MILLISECONDS);
                }
            }

            private TriggerContext getContext() {
                return new TriggerContext() {

                    @Override
                    public Date lastScheduledExecutionTime() {
                        return scheduledExecTime;
                    }

                    @Override
                    public Date lastCompletionTime() {
                        return completionTime;
                    }

                    @Override
                    public Date lastActualExecutionTime() {
                        return actualExecTime;
                    }
                };
            }

        };
    }

}
