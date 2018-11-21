package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;

public class ProgressBar extends ProgressTime {

	@Parameter(value = "false")
	private boolean percent;

	@SetupRender
	boolean setupRender() {
		return estimate > 0;
	}

	@Override
	@BeginRender
	void beginRender(MarkupWriter writer) {
		writer.writeRaw("<div class=\"progress\">");

		writer.writeRaw("<div aria-valuenow=\"" + getProgressPercent()
				+ "\" aria-valuemin=\"0\" aria-valuemax=\"100\""
				+ " class=\"progress-bar\" style=\"min-width:2em;width:"
				+ getProgressPercent()
				+ "%\">");
		if (percent)
			writer.writeRaw(getProgressPercent() + "%");
		else
			writer.writeRaw(toolsService.getTime(estimate));

		writer.writeRaw("</div>");

		writer.writeRaw("</div>");
	}

	private int getProgressPercent() {
		return 100 * progress / estimate;
	}

}
