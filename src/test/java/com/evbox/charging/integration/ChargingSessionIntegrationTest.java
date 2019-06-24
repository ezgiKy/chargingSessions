package com.evbox.charging.integration;

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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.evbox.charging.model.ChargingSessionRequest;
import com.evbox.charging.model.ChargingSessionsResponse;
import com.evbox.charging.model.ChargingSessionsSummaryResponse;
import com.evbox.charging.model.Status;
import com.evbox.charging.service.ChargingSessionService;
import com.evbox.charging.service.ChargingSessionSummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ChargingSessionIntegrationTest {
	
	@MockBean
    private ChargingSessionService chargingSessionService;
	
	@MockBean
    private ChargingSessionSummaryService chargingSessionSummaryService;
	
    @Autowired
    private MockMvc mockMvc;
	
	private static final Duration MIN = Duration.ofMinutes(1L);
	private static final Duration TWO_MIN = Duration.ofMinutes(2L);
	
	private static final String stationId1 = "1";
	private static final String stationId2 = "2";
	private static final String stationId3 = "3";

	private LocalDateTime now;
	private LocalDateTime twoMinBefore;
	
	@BeforeEach
	public void init() {
		now = LocalDateTime.now();
		twoMinBefore = LocalDateTime.now().minus(TWO_MIN);
	}
	
    
    @Test
    @DisplayName("Charging sessions started and should be returned from retrieveAll")
    void testRetrieveAllSubmittedChargingSession() throws Exception {
       	//given
    	ChargingSessionRequest chargingSessionRequest1 = new ChargingSessionRequest();
    	chargingSessionRequest1.setStationId(stationId1);
    	
    	
    	//when create first charging session
    	UUID uuid1= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession1 = new ChargingSessionsResponse (uuid1, stationId1, now(), Status.IN_PROGRESS);

		doReturn(chargingSession1).when(chargingSessionService).submit(stationId1);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest1)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession1.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession1.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession1.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession1.getStatus().toString())));
    	
    	
    	// when create second charging session
    	ChargingSessionRequest chargingSessionRequest2 = new ChargingSessionRequest();
    	chargingSessionRequest2.setStationId(stationId2);
    	
    	
    	UUID uuid2= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession2 = new ChargingSessionsResponse (uuid2, stationId2, now(), Status.IN_PROGRESS);

		doReturn(chargingSession2).when(chargingSessionService).submit(stationId2);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest2)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession2.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession2.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession2.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession2.getStatus().toString())));
    	
    	
		// given
		List<ChargingSessionsResponse> chargingSessions = new ArrayList<>();

		chargingSessions.add(chargingSession1);
		chargingSessions.add(chargingSession2);

		doReturn(chargingSessions).when(chargingSessionService).retrieveAll();
    	
    	// Execute the GET request
    	mockMvc.perform(get("/chargingSessions"))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", equalTo(chargingSessions.get(0).getId().toString())))
            .andExpect(jsonPath("$[0].stationId", equalTo(chargingSessions.get(0).getStationId())))
            .andExpect(jsonPath("$[0].updatedAt", equalTo(chargingSessions.get(0).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$[0].status", equalTo(chargingSessions.get(0).getStatus().toString())))
            .andExpect(jsonPath("$[1].id", equalTo(chargingSessions.get(1).getId().toString())))
            .andExpect(jsonPath("$[1].stationId", equalTo(chargingSessions.get(1).getStationId())))
            .andExpect(jsonPath("$[1].updatedAt", equalTo(chargingSessions.get(1).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$[1].status", equalTo(chargingSessions.get(1).getStatus().toString())));
    	
    }
    
    @Test
    @DisplayName("Charging sessions started  recently and should be in summary")
    void testRetrieveSummarySubmittedChargingSession() throws Exception {
       	//given
    	ChargingSessionRequest chargingSessionRequest1 = new ChargingSessionRequest();
    	chargingSessionRequest1.setStationId(stationId1);
    	
    	
    	//when create first charging session
    	UUID uuid1= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession1 = new ChargingSessionsResponse (uuid1, stationId1, now(), Status.IN_PROGRESS);

		doReturn(chargingSession1).when(chargingSessionService).submit(stationId1);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest1)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession1.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession1.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession1.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession1.getStatus().toString())));
    	
    	
    	// when create second charging session
    	ChargingSessionRequest chargingSessionRequest2 = new ChargingSessionRequest();
    	chargingSessionRequest2.setStationId(stationId2);
    	
    	
    	UUID uuid2= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession2 = new ChargingSessionsResponse (uuid2, stationId2, now(), Status.IN_PROGRESS);

		doReturn(chargingSession2).when(chargingSessionService).submit(stationId2);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest2)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession2.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession2.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession2.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession2.getStatus().toString())));
    	
    	
		// given
		ChargingSessionsSummaryResponse chargingSessionsSummary = new ChargingSessionsSummaryResponse(2L, 2L, 0L);

		
		//when
		doReturn(chargingSessionsSummary).when(chargingSessionSummaryService).retrieveSummary();
    	
    	// Execute the GET request
    	mockMvc.perform(get("/chargingSessions/summary"))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.totalCount", equalTo((int)chargingSessionsSummary.getTotalCount())))
            .andExpect(jsonPath("$.startedCount", equalTo((int)chargingSessionsSummary.getStartedCount())))
            .andExpect(jsonPath("$.stoppedCount", equalTo((int)chargingSessionsSummary.getStoppedCount())));  
    	
    }
    
    @Test
    @DisplayName("Expired charging sessions should not be in summary, recently stopped session should be in summary")
    void testRetrieveSummaryExpiredChargingSession() throws Exception {
       	//given
    	ChargingSessionRequest chargingSessionRequest1 = new ChargingSessionRequest();
    	chargingSessionRequest1.setStationId(stationId1);
    	
    	
    	//when create first charging session
    	UUID uuid1= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession1 = new ChargingSessionsResponse (uuid1, stationId1, twoMinBefore, Status.IN_PROGRESS);

		doReturn(chargingSession1).when(chargingSessionService).submit(stationId1);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest1)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession1.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession1.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession1.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession1.getStatus().toString())));
    	
    	
    	// when create second charging session
    	ChargingSessionRequest chargingSessionRequest2 = new ChargingSessionRequest();
    	chargingSessionRequest2.setStationId(stationId2);
    	
    	
    	UUID uuid2= UUID.randomUUID();
    	ChargingSessionsResponse chargingSession2 = new ChargingSessionsResponse (uuid2, stationId2, twoMinBefore, Status.IN_PROGRESS);

		doReturn(chargingSession2).when(chargingSessionService).submit(stationId2);
    	
    	// Execute the POST request
    	mockMvc.perform(post("/chargingSessions")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chargingSessionRequest2)))

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession2.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession2.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession2.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession2.getStatus().toString())));
    	
    	// stop first charging session
    	
    	//given
		String id1 = uuid1.toString();
		
		//when
		doReturn(chargingSession1).when(chargingSessionService).stop(id1);
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/chargingSessions/{id}", id1)
    			.contentType(MediaType.APPLICATION_JSON)
                )

                // Validate the response code 
                .andExpect(status().isOk())
    	
                // Validate the response 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", equalTo(chargingSession1.getId().toString())))
                .andExpect(jsonPath("$.stationId", equalTo(chargingSession1.getStationId())))
                .andExpect(jsonPath("$.updatedAt", equalTo(chargingSession1.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", equalTo(chargingSession1.getStatus().toString())));
    	
    	
    	
    	
		// given
		ChargingSessionsSummaryResponse chargingSessionsSummary = new ChargingSessionsSummaryResponse(1L, 0L, 1L);

		
		//when
		doReturn(chargingSessionsSummary).when(chargingSessionSummaryService).retrieveSummary();
    	
    	// Execute the GET request
    	mockMvc.perform(get("/chargingSessions/summary"))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.totalCount", equalTo((int)chargingSessionsSummary.getTotalCount())))
            .andExpect(jsonPath("$.startedCount", equalTo((int)chargingSessionsSummary.getStartedCount())))
            .andExpect(jsonPath("$.stoppedCount", equalTo((int)chargingSessionsSummary.getStoppedCount())));  
    	
    }
    
    
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
