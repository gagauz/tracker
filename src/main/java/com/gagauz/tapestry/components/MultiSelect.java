package com.gagauz.tapestry.components;

import com.gagauz.tapestry.encoder.CollectionValueEncoder;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.internal.util.SelectModelRenderer;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.util.EnumSelectModel;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 *
 * @author mg
 */
public class MultiSelect extends AbstractField {

    protected class Renderer extends SelectModelRenderer {

        public Renderer(MarkupWriter writer, ValueEncoder encoder) {
            super(writer, encoder);
        }

        @Override
        public boolean isOptionSelected(OptionModel optionModel, String clientValue) {
            return isSelected(clientValue);
        }
    }

    @Parameter
    protected ValueEncoder encoder;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Parameter(required = true, allowNull = false)
    protected SelectModel model;

    @Parameter(value = "auto", defaultPrefix = BindingConstants.LITERAL)
    private BlankOption blankOption;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String blankLabel;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "1")
    protected int size;

    @Inject
    private Request request;

    @Inject
    protected ComponentResources resources;

    @Environmental
    private ValidationTracker tracker;

    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    protected FieldValidator<Object> validate;

    @Parameter(required = true, principal = true, autoconnect = true)
    protected Collection<?> value;

    @Inject
    private FieldValidationSupport fieldValidationSupport;

    @Mixin
    private RenderDisabled renderDisabled;

    protected boolean isSelected(String clientValue) {
        return value != null && value.contains(encoder.toValue(clientValue));
    }

    @Override
    protected void processSubmission(String controlName) {
        String[] params = request.getParameters(controlName);
        Collection<?> submittedValue = toValue(params);

        putPropertyNameIntoBeanValidationContext("value");

        try {
            fieldValidationSupport.validate(submittedValue, resources, validate);

            value = submittedValue;
        } catch (ValidationException ex) {
            tracker.recordError(this, ex.getMessage());
        }

        removePropertyNameFromBeanValidationContext();
    }

    protected void afterRender(MarkupWriter writer) {
        writer.end();
    }

    protected void beginRender(MarkupWriter writer) {
        writer.element("select", "name", getControlName(), "id", getClientId(), "multiple", true, "size", size);

        putPropertyNameIntoBeanValidationContext("value");

        validate.render(writer);

        removePropertyNameFromBeanValidationContext();

        resources.renderInformalParameters(writer);

        decorateInsideField();

        // Disabled is via a mixin
    }

    protected Collection<?> toValue(String[] values) {
        return F.flow(values).map(new Mapper<String, Object>() {
            @Override
            public Object map(String element) {
                return encoder.toValue(element);
            }
        }).toList();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    SelectModel defaultModel() {
        Class valueType = (Class) ((ParameterizedType) resources.getBoundType("value").getGenericSuperclass()).getActualTypeArguments()[0];

        if (valueType == null) {
            return null;
        }

        if (Enum.class.isAssignableFrom(valueType)) {
            return new EnumSelectModel(valueType, resources.getContainerMessages());
        }

        return null;
    }

    ValueEncoder defaultEncoder() {
        return defaultProvider.defaultValueEncoder("value", resources);
    }

    Binding defaultValidate() {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

    Object defaultBlankLabel() {
        Messages containerMessages = resources.getContainerMessages();

        String key = resources.getId() + "-blanklabel";

        if (containerMessages.contains(key)) {
            return containerMessages.get(key);
        }

        return null;
    }

    @BeforeRenderTemplate
    protected void options(MarkupWriter writer) {
        if (showBlankOption()) {
            writer.element("option", "value", "");
            writer.write(blankLabel);
            writer.end();
        }

        SelectModelVisitor renderer = new Renderer(writer, encoder);

        model.visit(renderer);
    }

    @Override
    public boolean isRequired() {
        return validate.isRequired();
    }

    private boolean showBlankOption() {
        switch (blankOption) {
        case ALWAYS:
            return true;

        case NEVER:
            return false;

        default:
            return !isRequired();
        }
    }

    // For testing.

    void setModel(SelectModel model) {
        this.model = model;
        blankOption = BlankOption.NEVER;
    }

    void setValue(Collection<?> value) {
        this.value = value;
    }

    void setValueEncoder(CollectionValueEncoder<?> encoder) {
        this.encoder = encoder;
    }

    void setValidationTracker(ValidationTracker tracker) {
        this.tracker = tracker;
    }

    void setBlankOption(BlankOption option, String label) {
        blankOption = option;
        blankLabel = label;
    }
}
