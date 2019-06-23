package com.evbox.charging.controller;

import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.evbox.charging.model.ChargingSessionRequest;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.service.ChargingSessionService;
import com.evbox.charging.service.ChargingSessionSummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CharginSessionRestControllerTest {
	
	private static final String stationId1 = "1111";
	private static final String stationId2 = "1112";

	@MockBean
    private ChargingSessionService chargingSessionService;
	
	@MockBean
    private ChargingSessionSummaryService chargingSessionSummaryService;

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("POST /chargingSession - Success")
    void testSubmitChargingSession() throws Exception {
    	
    	//given
    	ChargingSessionRequest chargingSessionRequest = new ChargingSessionRequest();
    	chargingSessionRequest.setStationId(stationId1);
    	
    	
    	//when
    	ChargingSessionsResponse chargingSession = new ChargingSessionsResponse (UUID.randomUUID(), stationId1, now(), Status.IN_PROGRESS);

		doReturn(chargingSession).when(chargingSessionService).submit(stationId1);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSession")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession.getStatus().toString())));
    	
    }
    
    @Test
    @DisplayName("PUT /chargingSession - Success")
    void testStopChargingSession() throws Exception {
    	
    	//given
    	String id = stationId1;
    	
    	//when
    	ChargingSessionsResponse chargingSession = new ChargingSessionsResponse(UUID.randomUUID(), id, now(), Status.FINISHED);

		doReturn(chargingSession).when(chargingSessionService).stop(id);
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/chargingSession/{id}", id)
    			.contentType(MediaType.APPLICATION_JSON)
                )

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession.getStatus().toString())));
    	
    }
    
    @Test
    @DisplayName("GET /chargingSessions - Success")
    void testGetChargingSessions() throws Exception {
    	
		// given
		List<ChargingSessionsResponse> chargingSessions = new ArrayList<>();

		chargingSessions.add(new ChargingSessionsResponse(UUID.randomUUID(), stationId1, now(), Status.IN_PROGRESS));
		chargingSessions.add(new ChargingSessionsResponse(UUID.randomUUID(), stationId2, now(), Status.IN_PROGRESS));

		doReturn(chargingSessions).when(chargingSessionService).retrieveAll();
    	
    	// Execute the GET request
    	mockMvc.perform(get("/chargingSessions"))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.chargingSessions", hasSize(2)))
            .andExpect(jsonPath("$.chargingSessions[0].id", equalTo(chargingSessions.get(0).getId().toString())))
            .andExpect(jsonPath("$.chargingSessions[0].stationId", equalTo(chargingSessions.get(0).getStationId())))
            .andExpect(jsonPath("$.chargingSessions[0].updatedAt", equalTo(chargingSessions.get(0).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.chargingSessions[0].status", equalTo(chargingSessions.get(0).getStatus().toString())))
            .andExpect(jsonPath("$.chargingSessions[1].id", equalTo(chargingSessions.get(1).getId().toString())))
            .andExpect(jsonPath("$.chargingSessions[1].stationId", equalTo(chargingSessions.get(1).getStationId())))
            .andExpect(jsonPath("$.chargingSessions[1].updatedAt", equalTo(chargingSessions.get(1).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.chargingSessions[1].status", equalTo(chargingSessions.get(1).getStatus().toString())));
    	

    }
    
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
