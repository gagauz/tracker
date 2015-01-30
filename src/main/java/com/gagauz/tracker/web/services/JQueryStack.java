package com.gagauz.tracker.web.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JQueryStack implements JavaScriptStack {

    public static final String NAME = "jquery-stack";

    private final AssetSource assetSource;

    public JQueryStack(AssetSource assetSource) {
        this.assetSource = assetSource;
    }

    @Override
    public List<StylesheetLink> getStylesheets() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        final List<Asset> javaScriptStack = new ArrayList<Asset>();
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/jquery/jquery-1.11.1.js"));
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/jquery/jquery-ui.min.js"));
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/jquery/jquery-init.js"));
        //        javaScriptStack.add(assetSource.getExpandedAsset("context:/static/js/jquery/dropzone.js"));
        return javaScriptStack;
    }

    @Override
    public String getInitialization() {
        //Disable JS logging in production
        //        if (HostType.PRODUCTION == ConfigurationService.getHostType()) {
        //            return "window.trace = function() {};";
        //        }
        return null;
    }
}
