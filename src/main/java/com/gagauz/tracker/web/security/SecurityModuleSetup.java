package com.gagauz.tracker.web.security;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.security.api.*;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import java.io.IOException;

@ImportModule(SecurityModule.class)
public class SecurityModuleSetup {

    private static final String REDIRECT_PAGE = "___rdpg__";

    public static void bind(ServiceBinder binder) {
        binder.bind(RememberMeHandler.class);
        binder.bind(RememberMeFilter.class);
        binder.bind(AccessAttributeExtractor.class, AccessAttributeExtractorImpl.class);
        binder.bind(AccessAttributeChecker.class, AccessAttributeCheckerImpl.class);
    }

    public CookieEncryptorDecryptor buildSecurityEncryptor(@Inject @Symbol(SymbolConstants.HMAC_PASSPHRASE) String passphrase) {
        return new CookieEncryptorDecryptor(passphrase, "salt");
    }

    @Contribute(AuthenticationService.class)
    public void contributeAuthenticationService(OrderedConfiguration<AuthenticationHandler> configuration, @Inject RememberMeHandler rememberMeHandler, @Inject final Response response, @Inject final Cookies cookies) {
        configuration.add("RememberMeLoginHandler", rememberMeHandler, "before:*");
        configuration.add("RedirectHandler", new AuthenticationHandler<User, CredentialsImpl>() {

            @Override
            public void handleLogin(User user, CredentialsImpl credentials) {

                if (null != user) {
                    String redirect = cookies.readCookieValue(REDIRECT_PAGE);
                    cookies.removeCookieValue(REDIRECT_PAGE);
                    if (null != redirect) {
                        try {
                            response.sendRedirect(redirect);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void handleLogout(User user) {
            }
        }, "after:*");
    }

    @ServiceId("SecurityUserProvider")
    public UserProvider buildSessionUserService(@Inject final UserDao userDao) {
        return new UserProvider<User, CredentialsImpl>() {

            @Override
            public User findByCredentials(CredentialsImpl credentials) {

                User user = userDao.findByUsername(credentials.getUsername());
                if (null != user
                        && user.getPassword().equals(credentials.getPassword())) {
                    System.out.println(user.getRoleGroups());
                    userDao.evict(user);
                    return user;
                }
                return null;
            }
        };
    }

    @Contribute(AccessDeniedExceptionInterceptorFilter.class)
    public static void contributeSecurityExceptionInterceptorFilter(OrderedConfiguration<AccessDeniedHandler> configuration, @Inject final Request request, @Inject final Response response, @Inject final Cookies cookies) {
        configuration.add("redirector", new AccessDeniedHandler() {
            @Override
            public void handleException(AbstractCommonHandlerWrapper handlerWrapper, AccessDeniedException cause) {
                String page = null;
                if (handlerWrapper.getComponentEventRequestParameters() != null) {
                    page = handlerWrapper.getComponentEventRequestParameters().getActivePageName();
                }
                if (handlerWrapper.getPageRenderRequestParameters() != null) {
                    page = handlerWrapper.getPageRenderRequestParameters().getLogicalPageName();
                }
                try {
                    cookies.getBuilder(REDIRECT_PAGE, request.getPath()).write();
                    response.sendRedirect("/login");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "after:*");
    }
}
