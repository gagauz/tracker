package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.ExtensibleJavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.services.javascript.StackExtension;

import com.ivaga.tapestry.csscombiner.CssCombinerModule;
import com.ivaga.tapestry.csscombiner.LessModule;

@ImportModule({ LessModule.class, CssCombinerModule.class })
public class StorefrontStackModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(JavaScriptStack.class, ExtensibleJavaScriptStack.class).withId("AppJavaScriptStack");
	}

	@Decorate(serviceInterface = JavaScriptStackSource.class)
	public JavaScriptStackSource decorateJavaScriptStackSource(JavaScriptStackSource original) {
		return new JavaScriptStackSourceFilter(original);
	}

	@Contribute(JavaScriptStackSource.class)
	public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration,
			AssetSource assetSource,
			@Local @Inject JavaScriptStack appJavaScriptStack) {
		configuration.add("app", appJavaScriptStack);
	}

	@Local
	@Contribute(JavaScriptStack.class)
	public static void contributeAppJavaScriptStack(OrderedConfiguration<StackExtension> configuration) {
		configuration.add("combibed.less", StackExtension.stylesheet("/META-INF/assets/styles/combined.less"));
	}
}
