package com.evbox.charging.model.factory;

import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;

import com.evbox.charging.model.ChargingSession;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Configuration
public class ChargingSessionFactory {
	
	private final Map<UUID, ChargingSession> sessions;
	
	

}
