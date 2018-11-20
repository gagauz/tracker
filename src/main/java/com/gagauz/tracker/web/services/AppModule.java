package com.gagauz.tracker.web.services;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beaneditor.DataTypeConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.security.AuthenticationService;
import org.apache.tapestry5.security.LoginResult;
import org.apache.tapestry5.security.PrincipalStorage;
import org.apache.tapestry5.security.api.AccessAttributes;
import org.apache.tapestry5.security.api.AccessAttributesChecker;
import org.apache.tapestry5.security.api.AuthenticationHandler;
import org.apache.tapestry5.security.api.CookieCredentialEncoder;
import org.apache.tapestry5.security.api.Credentials;
import org.apache.tapestry5.security.api.UserProvider;
import org.apache.tapestry5.security.impl.CookieCredentials;
import org.apache.tapestry5.security.impl.UsernamePasswordCredentials;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.apache.tapestry5.web.services.modules.CoreWebappModule;
import org.apache.tapestry5.web.services.security.CookieEncryptorDecryptor;
import org.apache.tapestry5.web.services.security.SecuredAccessAttributeChecker;
import org.apache.tapestry5.web.services.security.SecuredAccessAttributes;
import org.apache.tapestry5.web.services.security.SecuredAnnotationModule;

import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.services.dao.UserDao;
import com.gagauz.tracker.services.scheduler.SchedulerService;
import com.gagauz.tracker.services.setup.TestDataInitializer;
import com.gagauz.tracker.utils.AppProperties;
import com.ivaga.tapestry.csscombiner.CssCombinerModule;
import com.ivaga.tapestry.csscombiner.LessModule;
import com.xl0e.tapestry.hibernate.HibernateModule;
import com.xl0e.util.Cast;
import com.xl0e.util.CryptoUtils;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
@ImportModule({
        CoreWebappModule.class,
        LessModule.class,
        CssCombinerModule.class,
        HibernateModule.class,
        SecuredAnnotationModule.class
})
public class AppModule {

    @Startup
    public static void initScenarios(@Inject TestDataInitializer ai, @Inject SchedulerService schedulerService) {
        if (AppProperties.FILL_TEST_DATA.getBoolean()) {
            ai.execute();
            schedulerService.update();
        }
    }

    public static void bind(ServiceBinder binder) {
    }

    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.override(SymbolConstants.HMAC_PASSPHRASE, "1.0-SNAPSHOT");
        configuration.override(ComponentParameterConstants.GRID_TABLE_CSS_CLASS, "table table-responsive");
    }

    @ApplicationDefaults
    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "ru,en");
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");
        configuration.add(SymbolConstants.COMBINE_SCRIPTS, true);
        configuration.add(SymbolConstants.ENABLE_HTML5_SUPPORT, true);
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        configuration.add(SymbolConstants.BOOTSTRAP_ROOT, "context:/static/bootstrap-3.3.6-dist");
        configuration.override(ComponentParameterConstants.GRID_TABLE_CSS_CLASS, "table table-responsive valign-center");
        configuration.override(SymbolConstants.FORM_GROUP_LABEL_CSS_CLASS, "col-sm-3 col-xs-12 control-label");
        configuration.override(SymbolConstants.FORM_GROUP_FORM_FIELD_WRAPPER_ELEMENT_CSS_CLASS, "col-sm-9 col-xs-12");
        configuration.override(SymbolConstants.FORM_GROUP_FORM_FIELD_WRAPPER_ELEMENT_NAME, "div");
        configuration.add(SymbolConstants.MINIFICATION_ENABLED, true);
        configuration.add(SymbolConstants.FORM_CLIENT_LOGIC_ENABLED, false);
        configuration.add(SymbolConstants.SECURE_ENABLED, true);
        configuration.add(SymbolConstants.FORM_FIELD_CSS_CLASS, "form-control");
        configuration.add(SymbolConstants.ENCODE_LOCALE_INTO_PATH, false);
        configuration.add(SymbolConstants.CHARSET, StandardCharsets.UTF_8.name());
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "aasdf785675sdfkjf^&*%ewrfw");
        configuration.add(UploadSymbols.FILESIZE_MAX, 5 * 1024 * 1024);
        configuration.add(UploadSymbols.REQUESTSIZE_MAX, -1);
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
            final AssetSource assetSource) {
    }

    @Decorate(serviceInterface = JavaScriptStackSource.class)
    public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
        return new JavaScriptStackSourceFilter(original);
    }

    @Decorate(serviceInterface = AccessAttributesChecker.class)
    public AccessAttributesChecker decorateAccessAttributesChecker(final AccessAttributesChecker<AccessAttributes> original) {
        return new SecuredAccessAttributeChecker() {
            @Override
            public boolean canAccess(final SecuredAccessAttributes sessionAttributes, final SecuredAccessAttributes resourceAttributes) {
                return (null != sessionAttributes && Optional.ofNullable(resourceAttributes)
                        .map(SecuredAccessAttributes::getAttributes)
                        .map(Collection::isEmpty)
                        .orElse(true))
                        || super.canAccess(sessionAttributes, resourceAttributes);
            }
        };
    }

    public static void contributeBeanBlockOverrideSource(Configuration<BeanBlockContribution> configuration) {
        configuration.add(new EditBlockContribution(DataTypeConstants.TEXT, "AppPropertyBlocks", "anytext"));
        configuration.add(new EditBlockContribution(DataTypeConstants.LONG_TEXT, "AppPropertyBlocks", "anytext"));
    }

    public static UserProvider<User, Credentials> buildUserProvider(@Inject final UserDao accountDao,
            @Inject final CookieEncryptorDecryptor cookieEncryptorDecryptor) {
        return c -> {

            return Cast.of(c)
                    .castF(UsernamePasswordCredentials.class, credentials -> {
                        return accountDao.findByNameAndPass(credentials.getUsername(), CryptoUtils.createSHA512String(credentials.getPassword()));
                    })
                    .castF(CookieCredentials.class, cr -> {
                        CookieCredentials credentials = (CookieCredentials) c;
                        String[] usernameAndPassword = cookieEncryptorDecryptor.decryptArray(credentials.getValue());
                        String username = usernameAndPassword[0];
                        String password = usernameAndPassword[1];
                        return accountDao.findByNameAndPass(username, password);
                    })
                    .get();

        };
    }

    public static CookieCredentialEncoder<User> buildCookieCredentialEncoder(@Inject final CookieEncryptorDecryptor cookieEncryptorDecryptor) {
        return user -> {
            String value = cookieEncryptorDecryptor.encryptArray(user.getUsername(), user.getPassword());
            return new CookieCredentials(value);
        };
    }

    @Contribute(AuthenticationService.class)
    public void contributeAuthenticationService(OrderedConfiguration<AuthenticationHandler> configuration,
            ApplicationStateManager applicationStateManager) {
        configuration.add("SetUserInSessionHandler", new AuthenticationHandler() {

            @Override
            public void handleLogin(LoginResult loginResult) {
                org.apache.tapestry5.security.api.User user = loginResult.getUser();

                if (loginResult.isSuccess() && null != user) {
                    PrincipalStorage storage = Optional.ofNullable(applicationStateManager.getIfExists(PrincipalStorage.class))
                            .orElseGet(PrincipalStorage::new);
                    storage.add(user);
                    applicationStateManager.set(PrincipalStorage.class, storage);
                    applicationStateManager.set(User.class, (User) user);
                }
            }

            @Override
            public void handleLogout(AccessAttributes user) {
            }
        }, "after:*");
    }

}
