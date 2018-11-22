package org.gagauz.tracker.web.utils;

public interface ZoneIdAware {
	default String getZoneId() {
		return getClass().getSimpleName() + "_zoneId";
	}
}
