package com.gagauz.tracker.web.mixins;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.annotations.BindParameter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.corelib.mixins.Autocomplete;
import org.apache.tapestry5.runtime.Component;

public class CoolAutocompleter extends Autocomplete {

    @InjectContainer
    private Component field;

    @BindParameter
    private FieldTranslator translate;

}
