package com.gagauz.tracker.web.services;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.ValueEncoderFactory;

public class ValueEncoderModule {

    public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration, final FeatureVersionDao featureVersionDao) {
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
