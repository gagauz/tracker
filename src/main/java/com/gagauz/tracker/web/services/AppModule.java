package com.gagauz.tracker.web.services;

import com.gagauz.tapestry.binding.CondBindingFactory;
import com.gagauz.tapestry.binding.DateBindingFactory;
import com.gagauz.tracker.beans.scheduler.SchedulerService;
import com.gagauz.tracker.beans.setup.TestDataInitializer;
import com.gagauz.tracker.web.security.SecurityModuleSetup;
import com.gagauz.tracker.web.services.hibernate.HibernateModule;
import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@ImportModule({SecurityModuleSetup.class, HibernateModule.class, TypeCoercerModule.class, ValueEncoderModule.class})
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
        configuration.override(ComponentParameterConstants.GRID_TABLE_CSS_CLASS, "table-responsive");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "ru,en");
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping(InternalConstants.CORE_LIBRARY, "org.gagauz.tapestry.common"));
        configuration.add(new LibraryMapping(InternalConstants.CORE_LIBRARY, "org.gagauz.tapestry.security"));
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration, final AssetSource assetSource) {
    }

    @Decorate(serviceInterface = JavaScriptStackSource.class)
    public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
        return new JavaScriptStackSourceFilter(original);
    }

    public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration, BindingSource bindingSource, TypeCoercer typeCoercer) {
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
        //        configuration.override("InjectDefaultStylesheet", null);
    }

    @Contribute(PartialMarkupRenderer.class)
    public void contributePartialMarkupRenderer(final OrderedConfiguration<PartialMarkupRendererFilter> configuration, final Environment environment) {

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

}
