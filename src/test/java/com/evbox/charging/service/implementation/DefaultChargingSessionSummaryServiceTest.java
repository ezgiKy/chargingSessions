package com.evbox.charging.service.implementation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.time.Duration;
import java.time.LocalDateTime;
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

import com.evbox.charging.model.ChargingSession;
import com.evbox.charging.model.ChargingSessionsSummaryResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.model.store.ChargingSessionStore;

@ExtendWith(MockitoExtension.class)
public class DefaultChargingSessionSummaryServiceTest {

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";
	private static final String stationId3 = "3";

	@InjectMocks
	DefaultChargingSessionSummaryService defaultChargingSessionSummaryService;

	@Mock
	private ChargingSessionStore chargingSessionStore;

	private static final Duration TWO_MIN = Duration.ofMinutes(2L);

	private LocalDateTime now;
	private LocalDateTime twoMinBefore;

	@BeforeEach
	public void init() {
		now = LocalDateTime.now();
		twoMinBefore = LocalDateTime.now().minus(TWO_MIN);
	}

	@Test
	@DisplayName("Empty summary should be returned")
	public void testEmptySummary() {

		// when
		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(0L));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(0L));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(0L));
	}

	@Test
	@DisplayName("Only one session started in last minute then summary should return as started")
	public void testOneNewSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 0L;
		long totalCount = 1L;

		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		final ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsSummaryResponse expectedResponse = new ChargingSessionsSummaryResponse(totalCount, startedCount,
				stoppedCount);

		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions started and stopped in last minute then summary should return them as started")
	public void testMultipleNewSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 2L;
		long totalCount = 3L;

		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		UUID uuid = UUID.randomUUID();

		ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new ChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new ChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsSummaryResponse expectedResponse = new ChargingSessionsSummaryResponse(totalCount, startedCount,
				stoppedCount);

		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions stopped in last minute then summary should return them as started")
	public void testMultipleNewStoppedSessionsSummary() {

		// given
		long startedCount = 0L;
		long stoppedCount = 2L;
		long totalCount = 2L;

		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsSummaryResponse expectedResponse = new ChargingSessionsSummaryResponse(totalCount, startedCount,
				stoppedCount);

		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions expired in last minute then summary should not return them")
	public void testExpiredSessionsSummary() {

		// given
		long startedCount = 0L;
		long stoppedCount = 0L;
		long totalCount = 0L;

		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setStatus(Status.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStoppedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsSummaryResponse expectedResponse = new ChargingSessionsSummaryResponse(totalCount, startedCount,
				stoppedCount);

		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("There are expired and recent sessions then summary should return recent sessions")
	public void testExpiredAndRecentSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 1L;
		long totalCount = 2L;

		Map<UUID, ChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		ChargingSession chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStoppedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);
		
		chargingSession = new ChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setStatus(Status.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		ChargingSessionsSummaryResponse expectedResponse = new ChargingSessionsSummaryResponse(totalCount, startedCount,
				stoppedCount);

		ChargingSessionsSummaryResponse chargingSessionsSummaryResponse = defaultChargingSessionSummaryService
				.retrieveSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

}
