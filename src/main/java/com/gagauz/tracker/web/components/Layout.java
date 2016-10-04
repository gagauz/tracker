package com.gagauz.tracker.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

/**
 * Layout component for pages of application tracker.
 */
public class Layout {

	public static final String MODAL_ID = "page_modal";
	public static final String MODAL_BODY_ID = "page_modal_body";

	@Component(parameters = { "id=prop:modalBodyId" })
	private Zone modalBodyZone;
	/**
	 * The page title, for the <title> element and the
	 * <h1>element.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	private String pageName;

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String sidebarTitle;

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private Block mainTitle;

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private Block sidebar;

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private Block header;

	@Property
	@Parameter
	private boolean hideLeftMenu;

	@Inject
	private ComponentResources resources;

	@Property
	@Inject
	@Symbol(SymbolConstants.APPLICATION_VERSION)
	private String appVersion;

	public String getClassForPageName() {
		return this.resources.getPageName().equalsIgnoreCase(this.pageName)
				? "current_page_item"
				: null;
	}

	public String[] getPageNames() {
		return new String[] { "Index", "About", "Contact" };
	}

	public String getModalId() {
		return MODAL_ID;
	}

	public String getModalBodyId() {
		return MODAL_BODY_ID;
	}
}
