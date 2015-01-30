package com.gagauz.tracker.web.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrackerStack implements JavaScriptStack {

    public static final String NAME = "tracker-stack";

    private final AssetSource assetSource;

    public TrackerStack(AssetSource assetSource) {
        this.assetSource = assetSource;
    }

    @Override
    public List<StylesheetLink> getStylesheets() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getStacks() {
        return Arrays.asList(JQueryStack.NAME);
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        final List<Asset> javaScriptStack = new ArrayList<Asset>();
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/popup.js"));
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/tapestry.js"));
        javaScriptStack.add(assetSource.getExpandedAsset("com/gagauz/tracker/web/stack/common.js"));
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
