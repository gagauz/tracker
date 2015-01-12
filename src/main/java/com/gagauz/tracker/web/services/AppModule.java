package com.gagauz.tracker.web.services;

import com.gagauz.tapestry.security.*;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.setup.TestDataInitializer;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.services.hibernate.HibernateModule2;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.*;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule({SecurityModule.class, HibernateModule2.class, TypeCoercerModule.class})
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
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "ru,en");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("tap", "com.gagauz.tapestry.common"));
        configuration.add(new LibraryMapping("security", "com.gagauz.tapestry.security"));
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

}
