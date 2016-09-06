package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.gagauz.tapestry.web.services.CommonEntityValueEncoderFactory;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

public class ValueEncoderModule {

    public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration,
            final FeatureVersionDao featureVersionDao) {
        configuration.add(User.class, new CommonEntityValueEncoderFactory(User.class));
        configuration.add(Project.class, new CommonEntityValueEncoderFactory(Project.class));
        configuration.add(Version.class, new CommonEntityValueEncoderFactory(Version.class));
        configuration.add(Feature.class, new CommonEntityValueEncoderFactory(Feature.class));
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
                                int versionId = Integer.parseInt(ids[1]);
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
        configuration.add(Ticket.class, new CommonEntityValueEncoderFactory(Ticket.class));
        configuration.add(TicketType.class, new CommonEntityValueEncoderFactory(TicketType.class));
        configuration.add(TicketComment.class, new CommonEntityValueEncoderFactory(TicketComment.class));
        configuration.add(RoleGroup.class, new CommonEntityValueEncoderFactory(RoleGroup.class));
        configuration.add(TicketStatus.class, new CommonEntityValueEncoderFactory(TicketStatus.class));
        configuration.add(Stage.class, new CommonEntityValueEncoderFactory(Stage.class));
    }
}
