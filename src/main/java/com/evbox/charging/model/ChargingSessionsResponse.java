package com.evbox.charging.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Value;

@Value
public class ChargingSessionsResponse {
	
	   private UUID id;

	    private String stationId;

	    private LocalDateTime updatedAt;

	    private Status status;

	    public static ChargingSessionsResponse of(final ChargingSession chargingSession) {
	        return new ChargingSessionsResponse(chargingSession.getId(),
	                chargingSession.getStationId(),
	                chargingSession.getUpdatedAt(),
	                chargingSession.getStatus());
	    }

}
