package org.apache.tapestry5.corelib.pages;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.gagauz.tapestry.common.model.EditDisplayContext;

public class PropertyEditBlocks {

    @Environmental
    private PropertyEditContext context;

    @Environmental
    private EditDisplayContext displayContext;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:textFieldTranslator", "validate=prop:textFieldValidator",
            "clientId=prop:context.propertyId", "annotationProvider=context"})
    private TextField textField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:numberFieldTranslator", "validate=prop:numberFieldValidator",
            "clientId=prop:context.propertyId", "annotationProvider=context"})
    private TextField numberField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label", "encoder=valueEncoderForProperty",
            "model=selectModelForProperty", "validate=prop:selectValidator",
            "clientId=prop:context.propertyId"})
    private Select select;

    @SuppressWarnings("unused")
    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "clientId=prop:context.propertyId"})
    private Checkbox checkboxField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label", "clientId=prop:context.propertyid",
            "validate=prop:dateFieldValidator"})
    private DateField dateField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label", "clientId=prop:context.propertyid",
            "validate=prop:calendarFieldValidator"})
    private DateField calendarField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:passwordFieldTranslator", "validate=prop:passwordFieldValidator",
            "clientId=prop:context.propertyId", "annotationProvider=context"})
    private PasswordField passwordField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:textAreaTranslator", "validate=prop:textAreaValidator",
            "clientId=prop:context.propertyId", "annotationProvider=context"})
    private TextArea textArea;

    @Inject
    private TypeCoercer typeCoercer;

    public PropertyEditContext getContext() {
        return context;
    }

    public EditDisplayContext getDisplayContext() {
        return displayContext;
    }

    public String getLabelClass() {
        return displayContext.getLabelClass();
    }

    public String getFieldClass() {
        return displayContext.getFieldClass();
    }

    public String getFieldWrapperClass() {
        return displayContext.getFieldWrapperClass();
    }

    public FieldTranslator getTextFieldTranslator() {
        return context.getTranslator(textField);
    }

    public FieldValidator getTextFieldValidator() {
        return context.getValidator(textField);
    }

    public FieldTranslator getNumberFieldTranslator() {
        return context.getTranslator(numberField);
    }

    public FieldValidator getNumberFieldValidator() {
        return context.getValidator(numberField);
    }

    public FieldTranslator getPasswordFieldTranslator() {
        return context.getTranslator(passwordField);
    }

    public FieldValidator getPasswordFieldValidator() {
        return context.getValidator(passwordField);
    }

    public FieldTranslator getTextAreaTranslator() {
        return context.getTranslator(textArea);
    }

    public FieldValidator getTextAreaValidator() {
        return context.getValidator(textArea);
    }

    public FieldValidator getDateFieldValidator() {
        return context.getValidator(dateField);
    }

    public FieldValidator getCalendarFieldValidator() {
        return context.getValidator(calendarField);
    }

    public FieldValidator getSelectValidator() {
        return context.getValidator(select);
    }

    /**
     * Provide a value encoder for an enum type.
     */
    @SuppressWarnings("unchecked")
    public ValueEncoder getValueEncoderForProperty() {
        return new EnumValueEncoder(typeCoercer, context.getPropertyType());
    }

    /**
     * Provide a select mode for an enum type.
     */
    @SuppressWarnings("unchecked")
    public SelectModel getSelectModelForProperty() {
        return new EnumSelectModel(context.getPropertyType(), context.getContainerMessages());
    }
}
