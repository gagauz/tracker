package com.gagauz.tracker.web.services;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ValueEncoderFactory;

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
                                                    final StageDao stageDao
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
    }

}
