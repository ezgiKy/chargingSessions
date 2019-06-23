package com.evbox.charging.model;

import lombok.Value;

@Value
public class ChargingSessionsSummaryResponse {

	private long totalCount;

	private long startedCount;

	private long stoppedCount;

	public ChargingSessionsSummaryResponse(long startedCount, long stoppedCount) {
		this.startedCount = startedCount;
		this.stoppedCount = stoppedCount;
		this.totalCount = startedCount + stoppedCount;
	}

}
