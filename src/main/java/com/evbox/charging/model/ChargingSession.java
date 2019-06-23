package com.evbox.charging.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ChargingSession {

	private UUID id;

	private String stationId;

	private LocalDateTime startedAt;

	private LocalDateTime stoppedAt;

	private LocalDateTime updatedAt;

	private Status status;
}


