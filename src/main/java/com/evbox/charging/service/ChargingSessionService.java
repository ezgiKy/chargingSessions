package com.evbox.charging.service;

import java.util.List;

import com.evbox.charging.model.ChargingSessionsResponse;

public interface ChargingSessionService {

	List<ChargingSessionsResponse> retrieveAll();

	ChargingSessionsResponse stop(String id);

	ChargingSessionsResponse submit(String stationId);

}
