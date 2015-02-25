package org.gagauz.tapestry.common.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Environment;
import org.gagauz.tapestry.common.model.EditDisplayContext;

@SupportsInformalParameters
@Events(EventConstants.PREPARE)
public class MyBeanEditForm implements ClientElement, FormValidationControl {

    /**
     * The text label for the submit button of the form, by default "Create/Update".
     */
    @Parameter(value = "message:submit-label", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String submitLabel;

    /**
     * The object to be edited. This will be read when the component renders and updated when the form for the component
     * is submitted. Typically, the container will listen for a "prepare" event, in order to ensure that a non-null
     * value is ready to be read or updated. Often, the BeanEditForm can create the object as needed (assuming a public,
     * no arguments constructor). The object property defaults to a property with the same name as the component id.
     */
    @Parameter(required = true, autoconnect = true)
    @Property
    private Object object;

    /**
     * A comma-separated list of property names to be retained from the
     * {@link org.apache.tapestry5.beaneditor.BeanModel} (only used
     * when a default model is created automatically).
     * Only these properties will be retained, and the properties will also be reordered. The names are
     * case-insensitive.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String include;

    /**
     * A comma-separated list of property names to be removed from the {@link org.apache.tapestry5.beaneditor.BeanModel}
     * (only used
     * when a default model is created automatically).
     * The names are case-insensitive.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String exclude;

    /**
     * A comma-separated list of property names indicating the order in which the properties should be presented. The
     * names are case insensitive. Any properties not indicated in the list will be appended to the end of the display
     * orde. Only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String reorder;

    /**
     * A comma-separated list of property names to be added to the {@link org.apache.tapestry5.beaneditor.BeanModel}
     * (only used
     * when a default model is created automatically).
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String add;

    @Component(parameters = "validationId=componentResources.id", publishParameters = "clientValidation,autofocus,zone")
    private Form form;

    /**
     * If set to true, then the form will include an additional button after the submit button labeled "Cancel".
     * The cancel button will submit the form, bypassing client-side validation. The BeanEditForm will fire a
     * {@link EventConstants#CANCELED} event (before the form's {@link EventConstants#VALIDATE} event).
     *
     * @since 5.2.0
     */
    @Property
    @Parameter
    private boolean cancel;

    /**
     * The model that identifies the parameters to be edited, their order, and every other aspect. If not specified, a
     * default bean model will be created from the type of the object bound to the object parameter. The add, include,
     * exclude and reorder parameters are <em>only</em> applied to a default model, not an explicitly provided one.
     */
    @Parameter
    @Property
    private BeanModel model;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String labelClass;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String fieldClass;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String fieldWrapperClass;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "form-group")
    @Property
    private String fieldRowClass;

    @Inject
    private ComponentResources resources;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Environment environment;

    public Runnable getSetup() {
        return new Runnable() {
            @Override
            public void run() {
                EditDisplayContext formContext = new EditDisplayContext();
                formContext.setLabelClass(labelClass);
                formContext.setFieldClass(fieldClass);
                formContext.setFieldWrapperClass(fieldWrapperClass);
                environment.push(EditDisplayContext.class, formContext);
            }

        };
    }

    public Runnable getCleanup() {
        return new Runnable() {
            @Override
            public void run() {
                environment.pop(EditDisplayContext.class);
            }
        };
    }

    boolean onPrepareFromForm() {

        resources.triggerEvent(EventConstants.PREPARE, null, null);

        if (model == null) {
            Class<?> beanType = resources.getBoundType("object");
            model = beanModelSource.createEditModel(beanType, resources.getContainerMessages());
            BeanModelUtils.modify(model, add, include, exclude, reorder);
        }

        return true;
    }

    /**
     * Returns the client id of the embedded form.
     */
    @Override
    public String getClientId() {
        return form.getClientId();
    }

    @Override
    public void clearErrors() {
        form.clearErrors();
    }

    @Override
    public boolean getHasErrors() {
        return form.getHasErrors();
    }

    @Override
    public boolean isValid() {
        return form.isValid();
    }

    @Override
    public void recordError(Field field, String errorMessage) {
        form.recordError(field, errorMessage);
    }

    @Override
    public void recordError(String errorMessage) {
        form.recordError(errorMessage);
    }
}
