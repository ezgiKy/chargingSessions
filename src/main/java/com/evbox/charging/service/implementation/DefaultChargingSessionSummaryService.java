package com.evbox.charging.service.implementation;

import org.springframework.stereotype.Service;

import com.evbox.charging.model.ChargingSessionsSummaryResponse;
import com.evbox.charging.model.factory.ChargingSessionFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultChargingSessionSummaryService {

	private final ChargingSessionFactory chargingSessionfactory;




	/**
	 * Retrieves sessions summary for the last minute. Time complexity is O(n).
	 *
	 * @return ChargingSessionsSummaryResponse
	 */
	public ChargingSessionsSummaryResponse retrieveSummary() {

		return new ChargingSessionsSummaryResponse(chargingSessionfactory.getSessions().size(), chargingSessionfactory.getSessions().size());
	}

}
