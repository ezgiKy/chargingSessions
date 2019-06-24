package com.evbox.charging.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChargingSessionRequest {

	@NotBlank
	private String stationId;

}
