package com.evbox.charging.model.store;

import java.util.Map;
import java.util.UUID;

import com.evbox.charging.model.ChargingSession;

import lombok.Data;

@Data
public class ChargingSessionStore {

	private final Map<UUID, ChargingSession> sessions;

}
