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
                                                    final TicketDao ticketDao,
                                                    final TicketCommentDao ticketCommentDao,
                                                    final RoleGroupDao roleGroupDao,
                                                    final TicketStatusDao ticketStatusDao,
                                                    final StageDao stageDao
            ) {
        configuration.add(User.class, new CommonEntityValueEncoderFactory(userDao));
        configuration.add(Project.class, new CommonEntityValueEncoderFactory(projectDao));
        configuration.add(Version.class, new CommonEntityValueEncoderFactory(versionDao));
        configuration.add(Feature.class, new CommonEntityValueEncoderFactory(featureDao));
        configuration.add(FeatureVersion.class, new CommonEntityValueEncoderFactory(featureVersionDao));
        configuration.add(Ticket.class, new CommonEntityValueEncoderFactory(ticketDao));
        configuration.add(TicketComment.class, new CommonEntityValueEncoderFactory(ticketCommentDao));
        configuration.add(RoleGroup.class, new CommonEntityValueEncoderFactory(roleGroupDao));
        configuration.add(TicketStatus.class, new CommonEntityValueEncoderFactory(ticketStatusDao));
        configuration.add(Stage.class, new CommonEntityValueEncoderFactory(stageDao));
    }

}
