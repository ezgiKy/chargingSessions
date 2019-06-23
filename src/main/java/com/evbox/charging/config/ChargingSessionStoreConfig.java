package com.evbox.charging.config;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.store.ChargingSessionStore;

@Configuration
public class ChargingSessionStoreConfig {
	
	
	@Bean
	public ChargingSessionStore chargingSessionStore() {
		return new ChargingSessionStore(new ConcurrentHashMap<UUID, ChargingSession>());
	}

}
