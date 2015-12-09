package com.gagauz.tracker.web.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.*;

public class JavaScriptStackSourceFilter implements JavaScriptStackSource {

    private Set<String> SKIP = new HashSet<String>(Arrays.asList("Slider", "AjaxUploadStack", "DataTableStack", "FormFragmentSupportStack", "FormSupportStack",
            "SuperfishStack", "JQueryDateFieldStack", "GalleryStack"));

    private class JavaScriptStackWraper implements JavaScriptStack {
        private final JavaScriptStack original;

        JavaScriptStackWraper(JavaScriptStack original) {
            this.original = original;
        }

        @Override
        public List<String> getStacks() {
            return original != null ? original.getStacks() : Collections.<String>emptyList();
        }

        @Override
        public List<Asset> getJavaScriptLibraries() {
            return original != null ? original.getJavaScriptLibraries() : Collections.<Asset>emptyList();
        }

        @Override
        public List<StylesheetLink> getStylesheets() {
            return Collections.<StylesheetLink>emptyList();
        }

        @Override
        public String getInitialization() {
            return original != null ? original.getInitialization() : null;
        }
    }

    private final JavaScriptStackSource original;

    public JavaScriptStackSourceFilter(JavaScriptStackSource original) {
        this.original = original;
    }

    @Override
    public JavaScriptStack getStack(String name) {
        JavaScriptStack stack = original.getStack(name);
        if (!SKIP.contains(stack.getClass().getSimpleName())) {
            return new JavaScriptStackWraper(stack);
        }
        return new JavaScriptStackWraper(null);
    }

    @Override
    public List<String> getStackNames() {
        return original.getStackNames();
    }

}
