package com.evbox.charging.model;

import lombok.Value;

@Value
public class ChargingSessionsSummaryResponse {

	private long totalCount;

	private long startedCount;

	private long stoppedCount;


}
