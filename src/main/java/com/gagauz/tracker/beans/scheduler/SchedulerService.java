package com.gagauz.tracker.beans.scheduler;

import com.gagauz.tracker.beans.common.HibernateSessionManager;
import com.gagauz.tracker.beans.dao.StageTriggerDao;
import com.gagauz.tracker.db.model.StageTrigger;
import com.gagauz.tracker.db.model.StageTrigger.Type;
import com.gagauz.tracker.utils.BashUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SchedulerService extends HibernateSessionManager {

    @Autowired
    private StageTriggerDao stageTriggerDao;

    @Autowired
    private SessionFactory sessionFactory;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10, this);

    @Transactional
    public void update() {
        updateNonTransactional();
    }

    public void updateNonTransactional() {
        executor.shutdownNow();
        List<StageTrigger> schedulers = stageTriggerDao.findEnabled();
        executor = Executors.newScheduledThreadPool(schedulers.size(), this);
        for (final StageTrigger scheduler : schedulers) {
            executor.execute(createRunnable(scheduler.getId(), new CronTrigger(scheduler.getCron())));
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
                        StageTrigger trigger = stageTriggerDao.findById(schedulerId);
                        System.out.println("Execute trigger " + trigger.getData());
                        if (trigger.getType() == Type.SCRIPT) {
                            StringBuilder sb = new StringBuilder();
                            BashUtils.execute(new File(trigger.getParent().getProject().getCvsRepositoryPath()),
                                    sb, trigger.getData().split("\n"));
                            System.out.println("--------------------------------------------------------------------");
                            System.out.println(sb.toString());
                            System.out.println("--------------------------------------------------------------------");
                        }
                    }
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

    @Override
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
