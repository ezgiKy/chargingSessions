package com.evbox.charging.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ChargingSessionRequest {

	@NotBlank
	private String stationId;

}
