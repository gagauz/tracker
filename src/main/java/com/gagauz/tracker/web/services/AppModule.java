package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beaneditor.DataTypeConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.security.PrincipalStorage;
import org.apache.tapestry5.security.api.AccessAttributesChecker;
import org.apache.tapestry5.security.api.Credentials;
import org.apache.tapestry5.security.api.User;
import org.apache.tapestry5.security.api.UserProvider;
import org.apache.tapestry5.security.impl.CookieCredentials;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.web.services.modules.CoreWebappModule;
import org.gagauz.tracker.web.security.AccessAttributeImpl;
import org.gagauz.tracker.web.security.CredentialsImpl;
import org.gagauz.tracker.web.security.Secured;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.beans.setup.TestDataInitializer;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.utils.AppProperties;
import com.ivaga.tapestry.csscombiner.CssCombinerModule;
import com.ivaga.tapestry.csscombiner.LessModule;
import com.xl0e.tapestry.hibernate.HibernateValueEncoderModule;
import com.xl0e.util.CryptoUtils;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
@ImportModule({ CoreWebappModule.class, LessModule.class, CssCombinerModule.class, HibernateValueEncoderModule.class })
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
        configuration.add(SymbolConstants.CHARSET, "utf-8");
        configuration.add(SymbolConstants.COMBINE_SCRIPTS, true);
        configuration.add(SymbolConstants.ENABLE_HTML5_SUPPORT, true);
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        configuration.add(SymbolConstants.BOOTSTRAP_ROOT, "context:/static/bootstrap-3.3.6-dist");
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
            final AssetSource assetSource) {
    }

    @Decorate(serviceInterface = JavaScriptStackSource.class)
    public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
        return new JavaScriptStackSourceFilter(original);
    }

    public static void contributeBeanBlockOverrideSource(Configuration<BeanBlockContribution> configuration) {
        configuration.add(new EditBlockContribution(DataTypeConstants.TEXT, "AppPropertyBlocks", "anytext"));
        configuration.add(new EditBlockContribution(DataTypeConstants.LONG_TEXT, "AppPropertyBlocks", "anytext"));
    }

    public static UserProvider buildUserProvider(final UserDao adminDao) {
        return new UserProvider() {
            @Override
            public <U extends User, C extends Credentials> U findByCredential(C arg0) {
                if (arg0 instanceof org.gagauz.tracker.web.security.CredentialsImpl) {
                    User user = adminDao.findByUsername(((org.gagauz.tracker.web.security.CredentialsImpl) arg0).getUsername());
                    if (null != user && user.checkPassword(((org.gagauz.tracker.web.security.CredentialsImpl) arg0).getPassword())) {
                        if (null != user.getRoleGroups()) {
                            user.getRoleGroups().forEach(System.out::println);
                        }
                        user = adminDao.unproxy(user);
                        return (U) user;
                    }
                    return null;
                } else if (arg0 instanceof CookieCredentials) {
                    try {
                        String[] tokens = CryptoUtils.decryptArrayAES(((CookieCredentials) arg0).getValue());
                        CredentialsImpl cred = new CredentialsImpl(tokens[0], tokens[1], false);
                        return findByCredential(cred);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }

            @Override
            public <U extends User, C extends Credentials> C toCredentials(U arg0, Class<C> arg1) {
                if (arg1.equals(CookieCredentials.class)) {
                    User user = (User) arg0;
                    String value = CryptoUtils.encryptArrayAES(user.getUsername(), user.getPassword(), user.getClass().getSimpleName());
                    return (C) new CookieCredentials(value);
                }
                throw new IllegalStateException();
            }

        };
    }

    public static AccessAttributesChecker buildAccessAttributeExtractorChecker() {
        return new AccessAttributesChecker<AccessAttributeImpl>() {

            @Override
            public boolean canAccess(PrincipalStorage userSet, AccessAttributeImpl attribute) {
                if (null != userSet) {
                    if (null == attribute || 0 == attribute.getRoles().length) {
                        return true;
                    }
                    return userSet.stream().filter(user -> ((User) user).checkRoles(attribute.getRoles())).findAny().isPresent();
                }
                return false;
            }

            @Override
            public AccessAttributeImpl extract(PlasticClass plasticClass, PlasticMethod plasticMethod) {

                if (null == plasticMethod) {
                    Secured annotation = plasticClass.getAnnotation(Secured.class);
                    if (null != annotation) {
                        return new AccessAttributeImpl(annotation.value());
                    }
                    return null;
                }

                Secured annotation = plasticMethod.getAnnotation(Secured.class);
                if (null != annotation) {
                    return new AccessAttributeImpl(annotation.value());
                }
                return null;
            }

        };
    }
}
