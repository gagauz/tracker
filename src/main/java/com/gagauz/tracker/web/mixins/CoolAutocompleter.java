package com.gagauz.tracker.web.mixins;

import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BindParameter;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.corelib.mixins.Autocomplete;
import org.apache.tapestry5.runtime.Component;

import java.util.List;

public class CoolAutocompleter extends Autocomplete {

    @InjectContainer
    private Component field;

    @BindParameter
    private FieldTranslator translate;

    /**
     * Generates the markup response that will be returned to the client; this should be an &lt;ul&gt; element with
     * nested &lt;li&gt; elements. Subclasses may override this to produce more involved markup (including images and
     * CSS class attributes).
     * 
     * @param writer  to write the list to
     * @param matches list of matching objects, each should be converted to a string
     */
    @Override
    protected void generateResponseMarkup(MarkupWriter writer, List matches) {

        writer.element("ul");
        for (Object o : matches) {
            writer.element("li");
            writer.write(((User) o).getName());
            writer.end();
        }

        writer.end(); // ul
    }
}
