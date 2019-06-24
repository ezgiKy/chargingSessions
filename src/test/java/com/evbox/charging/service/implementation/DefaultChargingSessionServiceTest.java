package com.evbox.charging.service.implementation;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evbox.charging.exception.DataNotFoundException;
import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.model.store.ChargingSessionStore;

@ExtendWith(MockitoExtension.class)
public class DefaultChargingSessionServiceTest {

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";
	private static final String stationId3 = "3";
	private static final String stationId4 = "4";

	private static final String id1 = "68a8a66d-6716-4c89-ad78-78efd58c7d2f";

	@InjectMocks
	DefaultChargingSessionService defaultChargingSessionService;

	@Mock
	private ChargingSessionStore chargingSessionStore;

	private LocalDateTime now;

	@BeforeEach
	public void init() {
		now = LocalDateTime.now();
	}

	@Test
	@DisplayName("Test submit a charging session with given stationId")
	public void testSubmitChargingSession() {

		// when
		ChargingSessionsResponse actualResponse = defaultChargingSessionService.submit(stationId1);

		// then
		assertEquals(Status.IN_PROGRESS, actualResponse.getStatus());
		assertEquals(stationId1, actualResponse.getStationId());
		assertThat(actualResponse.getId(), notNullValue());

	}

	@Test
	@DisplayName("Test stop a charging session with given id")
	public void testStopChargingSession() {

		// given
		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.fromString(id1);

		final ChargingSession chargingSession = new ChargingSession();

		chargingSession.setId(uuid);
		chargingSession.setStatus(Status.IN_PROGRESS);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);

		sessions.put(uuid, chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsResponse actualResponse = defaultChargingSessionService.stop(id1);

		// then
		assertEquals(uuid, actualResponse.getId());
		assertEquals(Status.FINISHED, actualResponse.getStatus());

	}

	@Test
	@DisplayName("Test stop a charging session with non existing id.")
	public void testStopNonExistingChargingSession() {

		// given
		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();

		// then
		assertThrows(DataNotFoundException.class, () -> defaultChargingSessionService.stop(id1));

	}

	@Test
	@DisplayName("Test retrieve all charging sessions")
	public void testRetrieveAllChargingSessions() {

		// given
		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		ChargingSession chargingSession = new ChargingSession();

		UUID uuid = UUID.randomUUID();

		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new ChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new ChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new ChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId4);
		chargingSession.setStatus(Status.FINISHED);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);

		sessions.put(uuid, chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		List<ChargingSessionsResponse> actualResponse = defaultChargingSessionService.retrieveAll();

		// then
		assertEquals(actualResponse.size(), sessions.size());

	}

	@Test
	@DisplayName("Test retrieve all with no charging sessions")
	public void testRetrieveAllEmptyChargingSessions() {

		// given
		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		List<ChargingSessionsResponse> actualResponse = defaultChargingSessionService.retrieveAll();

		// then
		assertEquals(actualResponse.size(), sessions.size());

	}

}
