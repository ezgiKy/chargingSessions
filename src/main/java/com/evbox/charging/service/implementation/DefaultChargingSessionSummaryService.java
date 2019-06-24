package com.evbox.charging.service.implementation;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.evbox.charging.model.ChargingSessionsSummaryResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.model.store.ChargingSessionStore;
import com.evbox.charging.service.ChargingSessionSummaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultChargingSessionSummaryService implements ChargingSessionSummaryService {

	private final ChargingSessionStore sessionStore;

	private static final Duration MIN = Duration.ofMinutes(1L);

	/**
	 * Retrieves sessions summary for the last minute. Time complexity is O(n).
	 *
	 * @return ChargingSessionsSummaryResponse
	 */
	public ChargingSessionsSummaryResponse retrieveSummary() {

		LocalDateTime oneMinBefore = LocalDateTime.now().minus(MIN);

		long startedCount = countStarted(oneMinBefore);

		long stoppedCount = countStopped(oneMinBefore);
		
		log.info("Charging session summary calculated as started: " + startedCount + " stopped: " + stoppedCount + ".");

		return new ChargingSessionsSummaryResponse(startedCount + stoppedCount, startedCount, stoppedCount);
	}

	private long countStopped(LocalDateTime oneMinBefore) {
		return sessionStore.getSessions().values().stream()
				.filter(s -> (s.getStatus() == Status.FINISHED && s.getStoppedAt().isAfter(oneMinBefore))).count();
	}

	private long countStarted(LocalDateTime oneMinBefore) {
		return sessionStore.getSessions().values().stream()
				.filter(s -> (s.getStartedAt().isAfter(oneMinBefore))).count();
	}

}
