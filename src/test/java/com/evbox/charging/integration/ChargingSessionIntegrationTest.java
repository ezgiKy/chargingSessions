package com.evbox.charging.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.evbox.charging.model.ChargingSessionRequest;
import com.evbox.charging.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ChargingSessionIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";

	@Test
	@DisplayName("Charging sessions started and should be returned from retrieveAll")
	void testRetrieveAllSubmittedChargingSession() throws Exception {
		// given
		ChargingSessionRequest chargingSessionRequest1 = new ChargingSessionRequest();
		chargingSessionRequest1.setStationId(stationId1);

		// Execute the POST request
		mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(chargingSessionRequest1)))

				// Validate the response code
				.andExpect(status().isOk())

				// Validate the response
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.stationId", equalTo(stationId1)))
				.andExpect(jsonPath("$.status", equalTo(Status.IN_PROGRESS.toString())));

		// when create second charging session
		ChargingSessionRequest chargingSessionRequest2 = new ChargingSessionRequest();
		chargingSessionRequest2.setStationId(stationId2);

		// Execute the POST request
		mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(chargingSessionRequest2)))

				// Validate the response code
				.andExpect(status().isOk())

				// Validate the response
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.stationId", equalTo(stationId2)))
				.andExpect(jsonPath("$.status", equalTo(Status.IN_PROGRESS.toString())));

		// Execute the GET request
		mockMvc.perform(get("/chargingSessions"))

				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				// Validate the returned fields
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].status", equalTo(Status.IN_PROGRESS.toString())))
				.andExpect(jsonPath("$[1].status", equalTo(Status.IN_PROGRESS.toString())));

		// Execute the GET request
		mockMvc.perform(get("/chargingSessions/summary"))

				// Validate the response code and content type
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				// Validate the returned fields
				.andExpect(jsonPath("$.totalCount", equalTo(2))).andExpect(jsonPath("$.startedCount", equalTo(2)))
				.andExpect(jsonPath("$.stoppedCount", equalTo(0)));

	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
