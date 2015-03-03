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
import com.gagauz.tracker.utils.StringUtils;
import com.gagauz.tracker.web.services.hibernate.HibernateModule;
import org.apache.tapestry5.*;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.services.CompositeFieldValidator;
import org.apache.tapestry5.internal.services.FieldValidatorDefaultSourceImpl;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
        configuration.override(ComponentParameterConstants.GRID_TABLE_CSS_CLASS, "table-responsive");
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
        configuration.add(new LibraryMapping(InternalConstants.CORE_LIBRARY, "org.gagauz.tapestry.common"));
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

    @Contribute(MarkupRenderer.class)
    public void contributeMarkupRenderer(final OrderedConfiguration<MarkupRendererFilter> configuration, final Environment environment) {

        MarkupRendererFilter validationDecorator = new MarkupRendererFilter() {
            @Override
            public void renderMarkup(final MarkupWriter markupWriter, MarkupRenderer renderer) {
                ValidationDecorator decorator = new BaseValidationDecorator() {
                    @Override
                    public void afterField(Field field) {
                        if (field == null)
                            return;

                        if (inError(field)) {
                            Element group = markupWriter.getElement().getContainer();
                            String clazz = group.getAttribute("class");
                            if (clazz != null && clazz.contains("form-group")) {
                                group.addClassName("has-error");
                            }
                        }
                    }

                    private boolean inError(Field field) {
                        ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
                        return tracker.inError(field);
                    }

                };
                environment.push(ValidationDecorator.class, decorator);
                renderer.renderMarkup(markupWriter);
                environment.pop(ValidationDecorator.class);
            }
        };

        configuration.override("ValidationDecorator", validationDecorator);
        configuration.override("InjectDefaultStylesheet", null);
    }

    @Contribute(PartialMarkupRenderer.class)
    public void contributePartialMarkupRenderer(final OrderedConfiguration<PartialMarkupRenderer> configuration, final Environment environment) {

        PartialMarkupRenderer validationDecorator = new PartialMarkupRenderer() {
            @Override
            public void renderMarkup(MarkupWriter writer, JSONObject reply) {
                ValidationDecorator decorator = new BaseValidationDecorator() {

                };
                environment.push(ValidationDecorator.class, decorator);
                // renderer.renderMarkup(markupWriter);
                environment.pop(ValidationDecorator.class);
            }

        };

        //        configuration.override("ValidationDecorator", validationDecorator);
        //        configuration.override("InjectDefaultStylesheet", null);
    }

    //@Decorate(serviceInterface = FieldValidatorDefaultSource.class)
    @Order("after:*")
    public static FieldValidatorDefaultSource decorateFieldValidatorDefaultSource(final FieldValidatorSource validationSource, final FieldValidatorDefaultSource defaultSource, final Environment environment) {
        return new FieldValidatorDefaultSourceImpl(null, null) {

            boolean isHibernateEntity(Class<?> clazz) {
                for (Annotation annotation : clazz.getAnnotations()) {
                    if (annotation instanceof Entity) {
                        return true;
                    }
                }
                return false;
            }

            Column getColumn(java.lang.reflect.Field clazz) {
                for (Annotation annotation : clazz.getAnnotations()) {
                    if (annotation instanceof Column) {
                        return (Column) annotation;
                    }
                }
                return null;
            }

            Column getColumn(java.lang.reflect.Method clazz) {
                for (Annotation annotation : clazz.getAnnotations()) {
                    if (annotation instanceof Column) {
                        return (Column) annotation;
                    }
                }
                return null;
            }

            JoinColumn getJoinColumn(java.lang.reflect.Field clazz) {
                for (Annotation annotation : clazz.getAnnotations()) {
                    if (annotation instanceof JoinColumn) {
                        return (JoinColumn) annotation;
                    }
                }
                return null;
            }

            JoinColumn getJoinColumn(java.lang.reflect.Method clazz) {
                for (Annotation annotation : clazz.getAnnotations()) {
                    if (annotation instanceof JoinColumn) {
                        return (JoinColumn) annotation;
                    }
                }
                return null;
            }

            @Override
            public FieldValidator createDefaultValidator(Field field, String overrideId, Messages overrideMessages,
                                                         Locale locale, Class propertyType, AnnotationProvider propertyAnnotations) {

                FieldValidator defaultValidator = defaultSource.createDefaultValidator(field, overrideId,
                        overrideMessages, locale, propertyType, propertyAnnotations);
                BeanValidationContext context = environment.peek(BeanValidationContext.class);
                if (null != context) {
                    Class<?> beanClass = context.getBeanType();
                    List<FieldValidator> validators = new LinkedList<FieldValidator>();
                    if (isHibernateEntity(beanClass)) {
                        Column column = null;
                        JoinColumn joinColumn = null;
                        try {
                            java.lang.reflect.Field field0 = beanClass.getField(overrideId);
                            column = getColumn(field0);
                            joinColumn = getJoinColumn(field0);
                        } catch (Exception e) {
                        }
                        if (null == column) {
                            try {
                                Method method = beanClass.getMethod("get" + StringUtils.capitalize(overrideId));
                                column = getColumn(method);
                                joinColumn = getJoinColumn(method);
                            } catch (Exception e) {
                            }
                        }
                        if (null != column) {
                            if (!column.nullable()) {
                                validators.add(validationSource.createValidator(field, "required", null));
                            }
                            if (column.length() > 0) {
                                validators.add(validationSource.createValidator(field, "maxlength", String.valueOf(column.length())));
                            }
                        }
                        if (null != joinColumn) {
                            if (!joinColumn.nullable()) {
                                validators.add(validationSource.createValidator(field, "required", null));
                            }
                        }
                        if (!validators.isEmpty()) {
                            return new CompositeFieldValidator(validators);
                        }
                    }

                }

                return defaultValidator;

            }

            @Override
            public FieldValidator createDefaultValidator(ComponentResources resources, String parameterName) {
                return defaultSource.createDefaultValidator(resources, parameterName);
            }
        };
    }
}
