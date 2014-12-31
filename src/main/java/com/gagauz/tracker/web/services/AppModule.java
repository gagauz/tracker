package com.gagauz.tracker.web.services;

import java.io.IOException;
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
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.hibernate.AssertionFailure;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.FeatureVersion.FeatureVersionId;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.web.services.security.Credentials;
import com.gagauz.tracker.web.services.security.SecurityModule;
import com.gagauz.tracker.web.services.security.SessionUser;
import com.gagauz.tracker.web.services.security.SessionUserService;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule({SecurityModule.class})
public class AppModule {

    @Startup
    public static void initScenarios(@Inject TestDataInitializer ai) {
        ai.execute();
    }

    public static void bind(ServiceBinder binder) {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.override(SymbolConstants.HMAC_PASSPHRASE, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    @ServiceId("TimingFilter")
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {
            @Override
            public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("tap", "com.gagauz.tapestry"));
    }

    @ServiceId("HibernateFilter")
    public RequestFilter buildHibernateFilter(final Logger log, final SessionFactory sessionFactory) {
        return new RequestFilter() {
            @Override
            public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
                boolean process = false;
                boolean participate = false;
                if (isUseHibernateSessionInRequest(request)) {
                    process = true;
                    log.info(String.format("Open hibernate session " + request.getPath()));
                    if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
                        // Do not modify the Session: just set the participate flag.
                        participate = true;
                        log.warn("Use existing Session in OpenSessionInViewFilter for " + request.getPath());
                    } else {
                        log.debug("Opening Hibernate Session in OpenSessionInViewFilter for " + request.getPath());
                        Session session = sessionFactory.openSession();
                        session.setFlushMode(FlushMode.MANUAL);
                        session.beginTransaction();
                        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
                    }
                }

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    if (process) {
                        if (!participate) {
                            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                            log.debug("Closing Hibernate Session in OpenSessionInViewFilter for " + request.getPath());
                            Session session = sessionHolder.getSession();
                            if (null != session && session.isOpen()) {

                                if (session.isDirty()) {
                                    session.flush();
                                }

                                Transaction transaction = null;
                                try {
                                    transaction = session.getTransaction();
                                    if (transaction.isActive()) {
                                        log.debug(String.format("try to commit transaction: %H in session: %H", transaction, session));
                                        transaction.commit();
                                    } else {
                                        log.warn(String.format("session: %H has no active transaction, path = %s!", session, request.getPath()));
                                    }
                                    session.close();
                                } catch (HibernateException e) {
                                    if (transaction != null) {
                                        transaction.rollback();
                                    }
                                    e.printStackTrace();
                                } catch (AssertionFailure af) {
                                    if (transaction != null) {
                                        transaction.rollback();
                                    }
                                    af.printStackTrace();
                                }
                            } else {
                                log.warn(String.format("session: %H is already closed!", session));
                            }

                        } else {
                            log.warn("Session used in OpenSessionInViewFilter for {} still open!", request.getPath());
                        }
                    }

                }
            }

            private boolean isUseHibernateSessionInRequest(Request request) {
                String path = request.getPath();
                return !(path.startsWith("/assets") || path.startsWith("/ajaxproxy"));
            }
        };
    }

    @ServiceId("")
    public SessionUserService buildSessionUserService(@Inject final UserDao userDao) {
        return new SessionUserService() {

            @Override
            public SessionUser loadByCredentials(Credentials credentials) {
                if (credentials.getToken() != null) {
                    User user = userDao.findByToken(credentials.getToken());
                    if (user != null) {
                        Role[] role = user.getRoles();
                        userDao.evict(user);
                    }
                    return user;
                }
                if (credentials.getUsername() != null) {
                    User user = userDao.findByUsername(credentials.getUsername());
                    if (null != user && user.getPassword().equals(credentials.getPassword())) {
                        Role[] role = user.getRoles();
                        userDao.evict(user);
                        return user;
                    }
                }
                return null;
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @InjectService("TimingFilter") RequestFilter timing,
                                         @InjectService("HibernateFilter") RequestFilter hibernate) {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Hibernate", hibernate, "before:*");
        configuration.add("Timing", timing);
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
