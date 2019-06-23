package com.evbox.charging.service.implementation;

import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.evbox.charging.config.ChargingSessionConfig;
import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionsSummaryResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultChargingSessionSummaryService {
	
	private final Map<UUID, ChargingSession> sessions;

	public DefaultChargingSessionSummaryService() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ChargingSessionConfig.class);
		sessions = (Map<UUID, ChargingSession>) context.getBean("ChargingSessionBean");

	}

	/**
	 * Retrieves sessions summary for the last minute. Time complexity is O(n).
	 *
	 * @return ChargingSessionsSummaryResponse
	 */
	public ChargingSessionsSummaryResponse retrieveSummary() {

		return new ChargingSessionsSummaryResponse(sessions.size(), sessions.size());
	}

}
