package com.gagauz.tracker.web.services;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.javascript.JavaScriptAggregationStrategy;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class JavaScriptStackSourceFilter implements JavaScriptStackSource {

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
			return original.getJavaScriptLibraries();
		}

		@Override
		public List<StylesheetLink> getStylesheets() {
			return Collections.<StylesheetLink>emptyList();
		}

		@Override
		public String getInitialization() {
			return original.getInitialization();
		}

		@Override
		public List<String> getModules() {
			return original.getModules();
		}

		@Override
		public JavaScriptAggregationStrategy getJavaScriptAggregationStrategy() {
			return original.getJavaScriptAggregationStrategy();
		}
	}

	private final JavaScriptStackSource original;
	private JavaScriptStack coreStackWrapper;

	public JavaScriptStackSourceFilter(JavaScriptStackSource original) {
		this.original = original;
	}

	@Override
	public JavaScriptStack getStack(String name) {
		if (name.equals(InternalConstants.CORE_STACK_NAME)) {
			if (null == coreStackWrapper) {
				coreStackWrapper = new JavaScriptStackWraper(original.getStack(name));
			}
			return coreStackWrapper;
		}
		return original.getStack(name);
	}

	@Override
	public List<String> getStackNames() {
		return original.getStackNames();
	}

	@Override
	public JavaScriptStack findStack(String name) {
		return original.findStack(name);
	}

	@Override
	public JavaScriptStack findStackForJavaScriptLibrary(Resource resource) {
		return original.findStackForJavaScriptLibrary(resource);
	}

}
