package com.gagauz.tracker.db.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gagauz.tracker.db.base.DB;
import com.gagauz.tracker.db.base.Model;

@Entity
@Table(name = DB.Table.canban_group)
public class CanbanGroup extends Model {
	private Project project;
	private String name;
	private int sort;
	private Set<TicketStatus> statuses;
	private String color;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public Set<TicketStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(Set<TicketStatus> statuses) {
		this.statuses = statuses;
	}

	@Column(length = 20)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
