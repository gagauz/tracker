package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.FieldValidatorSource;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.util.EnumValueEncoder;

import javax.persistence.Column;
import javax.persistence.Lob;

public class AppPropertyBlocks {

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:textFieldTranslator", "validate=prop:textFieldValidator",
            "clientId=prop:context.propertyId", "annotationProvider=context"})
    private TextField textField;

    @Component(parameters = {"value=context.propertyValue", "label=prop:context.label",
            "translate=prop:textAreaTranslator",
            "validate=prop:textAreaValidator", "clientId=prop:context.propertyId",
            "annotationProvider=context"})
    private TextArea textArea;

    @Inject
    private TypeCoercer typeCoercer;

    @Inject
    private FieldValidatorSource fieldValidatorSource;

    @Environmental
    private PropertyEditContext context;

    public PropertyEditContext getContext() {
        return context;
    }

    public FieldTranslator getTextFieldTranslator() {
        return context.getTranslator(textField);
    }

    public FieldValidator getTextFieldValidator() {
        return context.getValidator(textField);
    }

    public FieldTranslator getTextAreaTranslator() {
        return context.getTranslator(textArea);
    }

    public FieldValidator getTextAreaValidator() {
        return context.getValidator(textArea);
    }

    /**
    * Provide a value encoder for an enum type.
    */
    @SuppressWarnings("unchecked")
    public ValueEncoder getValueEncoderForProperty() {
        return new EnumValueEncoder(typeCoercer, context.getPropertyType());
    }

    //    public FieldValidator getNotNullValidator() {
    //
    //        return fieldValidatorSource.createValidators(field, expression);
    //    }

    public boolean isLong() {
        Column column = getContext().getAnnotation(Column.class);
        Lob lob = getContext().getAnnotation(Lob.class);
        return (null != column && column.length() > 255) || null != lob;
    }
}
