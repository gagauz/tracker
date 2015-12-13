package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ValueEncoderFactory;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.TicketCommentDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

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
        configuration.add(FeatureVersion.class, new ValueEncoderFactory<FeatureVersion>() {

            @Override
            public ValueEncoder<FeatureVersion> create(Class<FeatureVersion> type) {
                return new ValueEncoder<FeatureVersion>() {

                    @Override
                    public String toClient(FeatureVersion value) {
                        return null == value || null == value.getVersion() || null == value.getFeature()
                                ? null
                                : value.getFeature().getId() + "_" + value.getVersion().getId();
                    }

                    @Override
                    public FeatureVersion toValue(String clientValue) {
                        if (null != clientValue) {
                            try {
                                String[] ids = clientValue.split("_");
                                int featureId = Integer.parseInt(ids[0]);
                                int versionId = Integer.parseInt(ids[0]);
                                return featureVersionDao.findByFeatureAndVersion(featureId, versionId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                };
            }
        });
        configuration.add(Ticket.class, new CommonEntityValueEncoderFactory(ticketDao));
        configuration.add(TicketComment.class, new CommonEntityValueEncoderFactory(ticketCommentDao));
        configuration.add(RoleGroup.class, new CommonEntityValueEncoderFactory(roleGroupDao));
        configuration.add(TicketStatus.class, new CommonEntityValueEncoderFactory(ticketStatusDao));
        configuration.add(Stage.class, new CommonEntityValueEncoderFactory(stageDao));
    }
}
