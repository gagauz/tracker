package com.gagauz.tracker.db.base;

public interface DB {
	interface Table {
		String feature = "feature";
		String user_to_user_groups = "user_to_user_groups";
		String user_group = "user_group";
		String project = "project";
		String canban_group = "canban_group";
	}

	interface Column {
		String id = "id";
		String user_id = "user_id";
		String user_group_id = "user_group_id";
		String project_id = "project_id";
	}
}
