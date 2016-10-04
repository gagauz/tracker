package com.gagauz.tracker.web.components;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ModalLink extends AbstractLink {
	@Parameter(defaultPrefix = BindingConstants.COMPONENT, allowNull = false, required = true)
	private Zone zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String action;

	@Parameter
	private Object[] context;

	private String id;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private ComponentResources resources;

	void showModal() {
		ModalLink.this.ajaxResponseRenderer
				.addRender(Layout.MODAL_BODY_ID, ModalLink.this.zone.getBody())
				.addCallback(new JavaScriptCallback() {
					@Override
					public void run(JavaScriptSupport javascriptSupport) {
						javascriptSupport.require("modal").invoke("showModal").with(Layout.MODAL_ID);
					}
				});
	}

	@Ajax
	void onModal(EventContext context) {
		if (null != this.action) {
			this.resources.getContainerResources().triggerContextEvent(this.action, context, result -> {
				showModal();
				return true;
			});
		} else {
			showModal();
		}
	}

	void beginRender(MarkupWriter writer) {

		if (isDisabled())
			return;

		Link link = this.resources.createEventLink("modal", this.context);

		writeLink(writer, link);
		this.javaScriptSupport.require("t5/core/zone");
		writer.attributes("data-async-trigger", true);
	}

	void afterRender(MarkupWriter writer) {
		if (isDisabled())
			return;

		writer.end(); // <a>
	}

	public static class MutableEventContext implements EventContext {
		private EventContext source;

		public MutableEventContext(EventContext source) {
			this.source = source;
		}

		@Override
		public int getCount() {
			return this.source.getCount() - 1;
		}

		@Override
		public <T> T get(Class<T> desiredType, int index) {
			return this.source.get(desiredType, index);
		}

		@Override
		public String[] toStrings() {
			String[] array = this.source.toStrings();
			return ArrayUtils.subarray(array, 0, array.length - 1);
		}
	}
}
