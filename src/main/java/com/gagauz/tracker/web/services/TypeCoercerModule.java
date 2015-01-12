package com.gagauz.tracker.web.services;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.ValueEncoderFactory;

import java.util.*;

public class TypeCoercerModule {
    public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration,
                                                    final UserDao userDao,
                                                    final ProjectDao projectDao,
                                                    final VersionDao versionDao,
                                                    final FeatureDao featureDao,
                                                    final FeatureVersionDao featureVersionDao,
                                                    final TaskDao taskDao,
                                                    final RoleGroupDao roleGroupDao) {
        configuration.add(User.class, new ValueEncoderFactory<User>() {

            @Override
            public ValueEncoder<User> create(Class<User> arg0) {
                return new ValueEncoder<User>() {

                    @Override
                    public String toClient(User arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public User toValue(String arg0) {
                        return null == arg0 ? null : userDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });
        configuration.add(Project.class, new ValueEncoderFactory<Project>() {

            @Override
            public ValueEncoder<Project> create(Class<Project> arg0) {
                return new ValueEncoder<Project>() {

                    @Override
                    public String toClient(Project arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public Project toValue(String arg0) {
                        return null == arg0 ? null : projectDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });

        configuration.add(Version.class, new ValueEncoderFactory<Version>() {

            @Override
            public ValueEncoder<Version> create(Class<Version> arg0) {
                return new ValueEncoder<Version>() {

                    @Override
                    public String toClient(Version arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public Version toValue(String arg0) {
                        return null == arg0 ? null : versionDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });
        configuration.add(FeatureVersion.class, new ValueEncoderFactory<FeatureVersion>() {

            @Override
            public ValueEncoder<FeatureVersion> create(Class<FeatureVersion> arg0) {
                return new ValueEncoder<FeatureVersion>() {

                    @Override
                    public String toClient(FeatureVersion arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getFeature().getId() + "_" + arg0.getVersion().getId());
                    }

                    @Override
                    public FeatureVersion toValue(String arg0) {
                        if (null == arg0) {
                            return null;
                        }
                        String[] ids = arg0.split("_");
                        return featureVersionDao.findByFeatureAndVersion(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
                    }
                };
            }
        });
        configuration.add(Feature.class, new ValueEncoderFactory<Feature>() {

            @Override
            public ValueEncoder<Feature> create(Class<Feature> arg0) {
                return new ValueEncoder<Feature>() {

                    @Override
                    public String toClient(Feature arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public Feature toValue(String arg0) {
                        return null == arg0 ? null : featureDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });

        configuration.add(RoleGroup.class, new ValueEncoderFactory<RoleGroup>() {

            @Override
            public ValueEncoder<RoleGroup> create(Class<RoleGroup> arg0) {
                return new ValueEncoder<RoleGroup>() {

                    @Override
                    public String toClient(RoleGroup arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public RoleGroup toValue(String arg0) {
                        return null == arg0 ? null : roleGroupDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });

        configuration.add(Task.class, new ValueEncoderFactory<Task>() {

            @Override
            public ValueEncoder<Task> create(Class<Task> arg0) {
                return new ValueEncoder<Task>() {

                    @Override
                    public String toClient(Task arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public Task toValue(String arg0) {
                        return null == arg0 ? null : taskDao.findById(Integer.parseInt(arg0));
                    }
                };
            }
        });
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        Coercion<List, Set> coercion = new Coercion<List, Set>() {

            @Override
            public Set coerce(List input) {
                return new HashSet(input);
            }
        };

        Coercion<Collection, EnumSet> coercion1 = new Coercion<Collection, EnumSet>() {

            @Override
            public EnumSet coerce(Collection input) {
                return EnumSet.copyOf(input);
            }
        };

        configuration.add(new CoercionTuple<List, Set>(List.class, Set.class, coercion));
        configuration.add(new CoercionTuple<Collection, EnumSet>(Collection.class, EnumSet.class, coercion1));
    }
}
