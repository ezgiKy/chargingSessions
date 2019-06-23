package com.evbox.charging.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.evbox.charging.model.ChargingSessionRequest;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.service.ChargingSessionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class ChargingSessionRestController {

	private final ChargingSessionService chargingSessionService;
	
//	private final ChargingSessionSummaryService chargingSessionSummaryService;

	@PostMapping("/chargingSession")
	public ResponseEntity<ChargingSessionsResponse> createChargingSession(
			@RequestBody @Valid ChargingSessionRequest request) {
		return ok().body(chargingSessionService.submit(request.getStationId()));

	}

	@PutMapping("/chargingSession/{id}")
	public ResponseEntity<ChargingSessionsResponse> stopChargingSession(@PathVariable String id) {

		return ok().body(chargingSessionService.stop(id));

	}

	@GetMapping("/chargingSessions")
	public ResponseEntity<List<ChargingSessionsResponse>> getSessions() {
		return ok().body(chargingSessionService.retrieveAll());
	}

//	@GetMapping("/chargingSessions/summary")
//	public ResponseEntity<ChargingSessionsSummaryResponse> getSessionsSummary() {
//		return ok().body(chargingSessionSummaryService.retrieveSummary());
//	}

}
