package com.evbox.charging.config;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.evbox.charging.model.ChargingSession;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Data
public class ChargingSessionConfig {
	
	
	@Bean(name = "ChargingSessionBean")
	//private final Map<UUID, ChargingSession> sessions;
	public Map<UUID, ChargingSession>  getChargingSessions() {
		return new ConcurrentHashMap<UUID, ChargingSession>();
	}

}
