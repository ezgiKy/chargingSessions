package com.evbox.charging.service.implementation;

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

	/**
	 * Retrieves sessions summary for the last minute. Time complexity is O(n).
	 *
	 * @return ChargingSessionsSummaryResponse
	 */
	public ChargingSessionsSummaryResponse retrieveSummary() {

		long startedCount = sessionStore.getSessions().values().stream()
				.filter(s -> s.getStatus() == Status.IN_PROGRESS).count();

		long stoppedCount = sessionStore.getSessions().values().stream()
				.filter(s -> s.getStatus() == Status.FINISHED).count();
		
		return new ChargingSessionsSummaryResponse(startedCount, stoppedCount);
	}

}
