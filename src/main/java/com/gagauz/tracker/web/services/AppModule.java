package com.gagauz.tracker.web.services;

import com.gagauz.tapestry.binding.CondBindingFactory;
import com.gagauz.tapestry.binding.DateBindingFactory;
import com.gagauz.tapestry.security.*;
import com.gagauz.tapestry.security.api.*;
import com.gagauz.tapestry.security.impl.RedirectLoginHandler;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.beans.setup.TestDataInitializer;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.services.hibernate.HibernateModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule({SecurityModule.class, HibernateModule.class, TypeCoercerModule.class, ValueEncoderModule.class})
public class AppModule {

    @Startup
    public static void initScenarios(@Inject TestDataInitializer ai, @Inject SchedulerService schedulerService) {
        ai.execute();
        schedulerService.update();
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(RememberMeHandler.class);
        binder.bind(ToolsService.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.override(SymbolConstants.HMAC_PASSPHRASE, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "ru,en");
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");
        configuration.add(SymbolConstants.DEFAULT_STYLESHEET, "com/gagauz/tracker/web/stack/default.css");

        //Security config
        configuration.add(RedirectLoginHandler.SECURITY_REDIRECT_PARAMETER, "redirect");
        configuration.add(RedirectLoginHandler.SECURITY_REDIRECT_URL, "/login");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("tap", "com.gagauz.tapestry.common"));
        configuration.add(new LibraryMapping("security", "com.gagauz.tapestry.security"));
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
                                                       final AssetSource assetSource) {
        configuration.add(JQueryStack.NAME, new JQueryStack(assetSource));
        configuration.add(TrackerStack.NAME, new TrackerStack(assetSource));
        //configuration.override("InjectDefaultStylesheet", null);
    }

    @Contribute(SecurityExceptionInterceptorFilter.class)
    public void contributeSecurityExceptionInterceptorFilter(OrderedConfiguration<SecurityExceptionHandler> configuration, @Inject RedirectLoginHandler filter) {
        configuration.add("RedirectLoginHandler", filter);
    }

    @Contribute(LoginService.class)
    public void contributeLoginService(OrderedConfiguration<LoginHandler> configuration, @Inject RedirectLoginHandler filter) {
        configuration.add("RedirectLoginHandler", filter);
    }

    @Contribute(LogoutService.class)
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

    @Decorate(serviceInterface = JavaScriptStackSource.class)
    public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
        return new JavaScriptStackSourceFilter(original);
    }

    public SecurityEncryptor buildSecurityEncryptor(@Inject @Value("${" + SymbolConstants.HMAC_PASSPHRASE + "}") String passphrase) {
        return new SecurityEncryptor(passphrase);
    }

    public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration, BindingSource bindingSource, TypeCoercer typeCoercer) {
        configuration.add("cond", new CondBindingFactory(bindingSource, typeCoercer));
        configuration.add("date", new DateBindingFactory(bindingSource, typeCoercer));
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

    @Contribute(ServiceOverride.class)
    public static void overrideUrlEncoder(MappedConfiguration<Class<?>, Object> configuration) {
        configuration.add(URLEncoder.class, new URLEncoder() {

            @Override
            public String decode(String input) {
                return input;
            }

            @Override
            public String encode(String input) {
                return input;
            }
        });
    }

}
