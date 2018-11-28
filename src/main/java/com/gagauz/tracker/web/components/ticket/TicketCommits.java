package com.gagauz.tracker.web.components.ticket;

import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.gagauz.tracker.db.model.Commit;
import com.gagauz.tracker.db.model.Ticket;

@Import(module = { "bootstrap/collapse" })
public class TicketCommits {

	@Parameter
	private Ticket ticket;

	private List<Commit> commits;

	@Property
	private Commit commit;

	public List<Commit> getCommits() {
		if (null == commits) {
			// commits = cvsService.getCommits(ticket);
		}
		return commits;
	}

	public String formatDetails(String details) {
		if (null != details) {
			return details
					.replace("\n", "</li>")
					.replace("A\t", "<li class=\"a\">A ")
					.replace("M\t", "<li class=\"m\">M ")
					.replace("D\t", "<li class=\"d\">D ");
		}

		return "";
	}
}
