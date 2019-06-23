package com.evbox.charging.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evbox.charging.exception.DataNotFoundException;
import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.model.store.ChargingSessionStore;
import com.evbox.charging.service.ChargingSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultChargingSessionService implements ChargingSessionService {

	private final ChargingSessionStore sessionStore;

	@Override
	public ChargingSessionsResponse submit(String stationId) {
		final LocalDateTime startedAt = LocalDateTime.now();
		final UUID uuid = UUID.randomUUID();

		final ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId);
		chargingSession.setStartedAt(startedAt);
		chargingSession.setUpdatedAt(startedAt);
		chargingSession.setStatus(Status.IN_PROGRESS);

		sessionStore.getSessions().put(uuid, chargingSession);

		return ChargingSessionsResponse.of(chargingSession);
	}

	@Override
	public ChargingSessionsResponse stop(final String id) {
		final UUID uuid = UUID.fromString(id);

		final ChargingSession chargingSession = sessionStore.getSessions().values().stream()
				.filter(s -> (uuid.equals(s.getId()) && s.getStatus() == Status.IN_PROGRESS)).findAny()
				.orElseThrow(() -> new DataNotFoundException("There is no in progress session with id: " + id));

		final LocalDateTime stoppedAt = LocalDateTime.now();

		chargingSession.setStatus(Status.FINISHED);
		chargingSession.setUpdatedAt(stoppedAt);
		chargingSession.setStoppedAt(stoppedAt);

		return ChargingSessionsResponse.of(chargingSession);
	}

	@Override
	public List<ChargingSessionsResponse> retrieveAll() {
		System.out.println(sessionStore.getSessions().values().stream().count());
		return sessionStore.getSessions().values().stream().map(ChargingSessionsResponse::of)
				.collect(Collectors.toList());
	}

}
