package com.gagauz.tracker.web.services;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.ValueEncoderFactory;

import com.gagauz.tapestry.security.Credentials;
import com.gagauz.tapestry.security.LogoutHandler;
import com.gagauz.tapestry.security.SecurityEncryptor;
import com.gagauz.tapestry.security.SecurityModule;
import com.gagauz.tapestry.security.SecurityUser;
import com.gagauz.tapestry.security.SecurityUserProvider;
import com.gagauz.tracker.beans.dao.BugDao;
import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.beans.setup.TestDataInitializer;
import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.web.services.hibernate.HibernateModule2;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule({SecurityModule.class, HibernateModule2.class})
public class AppModule {

    @Startup
    public static void initScenarios(@Inject TestDataInitializer ai) {
        ai.execute();
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(RememberMeHandler.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.override(SymbolConstants.HMAC_PASSPHRASE, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("tap", "com.gagauz.tapestry"));
    }

    public static void contributeLogoutService(OrderedConfiguration<LogoutHandler> configuration, RememberMeHandler handler) {
        configuration.add("RememberMeLogoutHandler", handler);
    }

    @Contribute(ComponentEventRequestHandler.class)
    public static void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration, RememberMeHandler handler) {
        configuration.add("RememberMeHandler1", handler);
    }

    @Contribute(PageRenderRequestHandler.class)
    public static void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration, RememberMeHandler handler) {
        configuration.add("RememberMeHandler2", handler);
    }

    public SecurityEncryptor buildSecurityEncryptor(@Inject @Value("${" + SymbolConstants.HMAC_PASSPHRASE + "}") String passphrase) {
        return new SecurityEncryptor(passphrase);
    }

    @ServiceId("SecurityUserProvider")
    public SecurityUserProvider buildSessionUserService(@Inject final UserDao userDao) {
        return new SecurityUserProvider() {

            @Override
            public SecurityUser loadByCredentials(Credentials credentials) {
                if (credentials instanceof CredentialsImpl) {
                    CredentialsImpl credentialsImpl = (CredentialsImpl) credentials;
                    User user = userDao.findByUsername(credentialsImpl.getUsername());
                    if (null != user && user.getPassword().equals(credentialsImpl.getPassword())) {
                        Role[] role = user.getRoles();
                        userDao.evict(user);
                        return user;
                    }
                }
                return null;
            }
        };
    }

    public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration,
                                                    final UserDao userDao,
                                                    final ProjectDao projectDao,
                                                    final VersionDao versionDao,
                                                    final FeatureDao featureDao,
                                                    final FeatureVersionDao taskDao,
                                                    final RoleGroupDao roleGroupDao,
                                                    final BugDao bugDao) {
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
        /*
                configuration.add(FeatureVersion.class, new ValueEncoderFactory<FeatureVersion>() {

                    @Override
                    public ValueEncoder<FeatureVersion> create(Class<FeatureVersion> arg0) {
                        return new ValueEncoder<FeatureVersion>() {

                            @Override
                            public String toClient(FeatureVersion arg0) {
                                return null == arg0 ? null : String.valueOf(arg0.getId().getFeatureId() + "_" + arg0.getId().getVersionId());
                            }

                            @Override
                            public FeatureVersion toValue(String arg0) {
                                if (null == arg0) {
                                    return null;
                                }
                                String[] ids = arg0.split("_");
                                return taskDao.findById(new FeatureVersionId(Integer.parseInt(ids[0]), Integer.parseInt(ids[1])));
                            }
                        };
                    }
                });
        */
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

        configuration.add(Bug.class, new ValueEncoderFactory<Bug>() {

            @Override
            public ValueEncoder<Bug> create(Class<Bug> arg0) {
                return new ValueEncoder<Bug>() {

                    @Override
                    public String toClient(Bug arg0) {
                        return null == arg0 ? null : String.valueOf(arg0.getId());
                    }

                    @Override
                    public Bug toValue(String arg0) {
                        return null == arg0 ? null : bugDao.findById(Integer.parseInt(arg0));
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
