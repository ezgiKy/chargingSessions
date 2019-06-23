package com.evbox.charging.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evbox.charging.exception.DataNotFoundException;
import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.model.factory.ChargingSessionFactory;
import com.evbox.charging.service.ChargingSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultChargingSessionService implements ChargingSessionService {

	private final ChargingSessionFactory chargingSessionfactory;

	// private final Map<UUID, ChargingSession> sessions;

	@Override
	public List<ChargingSessionsResponse> retrieveAll() {
		System.out.println(chargingSessionfactory.getSessions().values().stream().count());
		return chargingSessionfactory.getSessions().values().stream().map(ChargingSessionsResponse::from)
				.collect(Collectors.toList());
	}

	@Override
	public ChargingSessionsResponse stop(final String id) {
		final UUID uuid = UUID.fromString(id);

		final ChargingSession chargingSession = chargingSessionfactory.getSessions().values().stream()
				.filter(s -> (uuid.equals(s.getId()) && s.getStatus() == Status.IN_PROGRESS)).findAny()
				.orElseThrow(() -> new DataNotFoundException("There is no in progress session with id: " + id));

		final LocalDateTime stoppedAt = LocalDateTime.now();

		chargingSession.setStatus(Status.FINISHED);
		chargingSession.setUpdatedAt(stoppedAt);
		chargingSession.setStoppedAt(stoppedAt);

		return ChargingSessionsResponse.from(chargingSession);
	}

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

		chargingSessionfactory.getSessions().put(uuid, chargingSession);

		return ChargingSessionsResponse.from(chargingSession);
	}

}
