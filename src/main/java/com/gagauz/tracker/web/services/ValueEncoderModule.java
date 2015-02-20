package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ValueEncoderFactory;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.StageActionDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.StageTriggerDao;
import com.gagauz.tracker.beans.dao.TaskCommentDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.StageAction;
import com.gagauz.tracker.db.model.StageTrigger;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskComment;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

public class ValueEncoderModule {

    public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration,
                                                    final UserDao userDao,
                                                    final ProjectDao projectDao,
                                                    final VersionDao versionDao,
                                                    final FeatureDao featureDao,
                                                    final FeatureVersionDao featureVersionDao,
                                                    final TaskDao taskDao,
                                                    final TaskCommentDao taskCommentDao,
                                                    final RoleGroupDao roleGroupDao,
                                                    final StageDao stageDao,
                                                    final StageTriggerDao stageTriggerDao,
                                                    final StageActionDao stageActionDao
            ) {
        configuration.add(User.class, new CommonEntityValueEncoderFactory(userDao));
        configuration.add(Project.class, new CommonEntityValueEncoderFactory(projectDao));
        configuration.add(Version.class, new CommonEntityValueEncoderFactory(versionDao));
        configuration.add(Feature.class, new CommonEntityValueEncoderFactory(featureDao));
        configuration.add(FeatureVersion.class, new CommonEntityValueEncoderFactory(featureVersionDao));
        configuration.add(Task.class, new CommonEntityValueEncoderFactory(taskDao));
        configuration.add(TaskComment.class, new CommonEntityValueEncoderFactory(taskCommentDao));
        configuration.add(RoleGroup.class, new CommonEntityValueEncoderFactory(roleGroupDao));
        configuration.add(Stage.class, new CommonEntityValueEncoderFactory(stageDao));
        configuration.add(StageAction.class, new CommonEntityValueEncoderFactory(stageActionDao));
        configuration.add(StageTrigger.class, new CommonEntityValueEncoderFactory(stageTriggerDao));
    }

}
