package com.gagauz.tracker.web.components;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.TicketDao;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;

public class VersionStat {

	private int margin = 5;

	@Parameter(required = true, allowNull = false)
	private Version version;

	@Property
	private Entry<TicketStatus, Integer> stat;

	@Inject
	private TicketDao ticketDao;

	@Cached
	public Map<TicketStatus, Long> getStatistics() {
		return ticketDao.getVersionStatistics(version);
	}

	public String getPieChart(int size) {
		Map<TicketStatus, Long> stats = getStatistics();
		double total = stats.values().stream().mapToDouble(l -> l).sum();
		String html = "<svg width='" + size + "' height='" + size + "'>";
		double a = 0;

		int r = size / 2 - margin;
		Random rand = new Random(System.currentTimeMillis());
		for (Map.Entry<TicketStatus, Long> e : stats.entrySet()) {
			long[] start = getXY(r, a);
			a += e.getValue() / total;
			long[] end = getXY(r, a);
			html += "<path d='";
			// Move to center
			html += "M" + r + " " + r + " ";
			// Line to start point
			html += "L" + start[0] + " " + start[1] + " ";
			// Arc to end point
			html += "A " + r + " " + r + " 0 0 1 " + end[0] + " " + end[1] + " ";
			// Line to center
			html += "L" + r + " " + r + " Z' ";
			// Fill
			html += "style='fill:" + e.getKey().getColor() + ";stroke:rgb(0,0,0)'/>";
		}
		html += "</svg>";
		return html;
	}

	public long[] getXY(int r, double a) {
		return new long[] { margin + r + round(r * cos(2 * PI * a)), margin + r + round(r * sin(2 * PI * a)) };
	}
}
