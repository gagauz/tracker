package com.gagauz.tracker.web.services;

import org.apache.catalina.users.AbstractUser;
import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.beaneditor.DataTypeConstants;
import org.apache.tapestry5.internal.InternalConstants;
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
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.URLEncoder;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.gagauz.tapestry.security.api.Credentials;
import org.gagauz.tapestry.security.api.User;
import org.gagauz.tapestry.security.api.UserProvider;
import org.gagauz.tapestry.security.impl.CookieCredentials;
import org.gagauz.tapestry.web.services.CoreWebappModule;
import org.gagauz.tracker.web.security.CredentialsImpl;
import org.gagauz.utils.CryptoUtils;

import com.gagauz.tapestry.binding.CondBindingFactory;
import com.gagauz.tapestry.binding.DateBindingFactory;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.beans.setup.TestDataInitializer;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
@ImportModule({ ValueEncoderModule.class, CoreWebappModule.class })
public class AppModule {

    @Startup
    public static void initScenarios(@Inject TestDataInitializer ai, @Inject SchedulerService schedulerService) {
        ai.execute();
        schedulerService.update();
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(ToolsService.class);
    }

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

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping(InternalConstants.CORE_LIBRARY, "org.gagauz.tapestry.common"));
        configuration.add(new LibraryMapping(InternalConstants.CORE_LIBRARY, "org.gagauz.tapestry.security"));
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
            final AssetSource assetSource) {
    }

    @Decorate(serviceInterface = JavaScriptStackSource.class)
    public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
        return new JavaScriptStackSourceFilter(original);
    }

    public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration, BindingSource bindingSource,
            TypeCoercer typeCoercer) {
        configuration.add("cond", new CondBindingFactory(bindingSource, typeCoercer));
        configuration.add("date", new DateBindingFactory(bindingSource, typeCoercer));
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

    @Contribute(MarkupRenderer.class)
    public void contributeMarkupRenderer(final OrderedConfiguration<MarkupRendererFilter> configuration, final Environment environment) {

        MarkupRendererFilter validationDecorator = new MarkupRendererFilter() {
            @Override
            public void renderMarkup(final MarkupWriter markupWriter, MarkupRenderer renderer) {
                ValidationDecorator decorator = new AppValidationDecorator(markupWriter,
                        environment);
                environment.push(ValidationDecorator.class, decorator);
                renderer.renderMarkup(markupWriter);
                environment.pop(ValidationDecorator.class);
            }
        };

        configuration.override("ValidationDecorator", validationDecorator);
        // configuration.override("InjectDefaultStylesheet", null);
    }

    @Contribute(PartialMarkupRenderer.class)
    public void contributePartialMarkupRenderer(final OrderedConfiguration<PartialMarkupRendererFilter> configuration,
            final Environment environment) {

        PartialMarkupRendererFilter validationDecorator = new PartialMarkupRendererFilter() {
            @Override
            public void renderMarkup(MarkupWriter markupWriter, JSONObject reply, PartialMarkupRenderer renderer) {
                ValidationDecorator decorator = new AppValidationDecorator(markupWriter,
                        environment);
                environment.push(ValidationDecorator.class, decorator);
                renderer.renderMarkup(markupWriter, reply);
                environment.pop(ValidationDecorator.class);
            }
        };

        configuration.override("ValidationDecorator", validationDecorator);
    }

    public static void contributeBeanBlockOverrideSource(Configuration<BeanBlockContribution> configuration) {
        configuration.add(new EditBlockContribution(DataTypeConstants.TEXT, "AppPropertyBlocks", "anytext"));
        configuration.add(new EditBlockContribution(DataTypeConstants.LONG_TEXT, "AppPropertyBlocks", "anytext"));
    }

    public static UserProvider buildUserProvider(final UserDao adminDao) {
        return new UserProvider() {
            @Override
            public <U extends User, C extends Credentials> U findByCredentials(C arg0) {
                if (arg0 instanceof org.gagauz.tracker.web.security.CredentialsImpl) {
                    com.gagauz.tracker.db.model.User user = adminDao
                            .findByUsername(((org.gagauz.tracker.web.security.CredentialsImpl) arg0).getUsername());
                    if (null != user && user.checkPassword(((org.gagauz.tracker.web.security.CredentialsImpl) arg0).getPassword())) {
                        return (U) user;
                    }
                    return null;
                } else if (arg0 instanceof CookieCredentials) {
                    try {
                        String[] tokens = CryptoUtils.decryptArrayAES(((CookieCredentials) arg0).getValue());
                        CredentialsImpl cred = new CredentialsImpl(tokens[0], tokens[1], false);
                        return findByCredentials(cred);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }

            @Override
            public <U extends User, C extends Credentials> C toCredentials(U arg0, Class<C> arg1) {
                if (arg1.equals(CookieCredentials.class)) {
                    AbstractUser user = (AbstractUser) arg0;
                    String value = CryptoUtils.encryptArrayAES(user.getUsername(), user.getPassword(),
                            user.getClass().getSimpleName());
                    return (C) new CookieCredentials(value);
                }
                throw new IllegalStateException();
            }

        };
    }
}
