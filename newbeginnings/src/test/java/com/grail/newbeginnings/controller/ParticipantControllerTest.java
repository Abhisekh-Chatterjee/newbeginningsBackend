package com.grail.newbeginnings.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.grail.newbeginnings.model.Participant;
import com.grail.newbeginnings.repository.ParticipantRepository;
import com.grail.newbeginnings.service.ParticipantService;
import com.grail.newbeginnings.service.ParticipantServiceImpl;
import com.grail.newbeginnings.service.ReferenceNumberGeneratorService;
import com.grail.newbeginnings.service.ReferenceNumberGeneratorServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ParticipantControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@Mock
	private ParticipantServiceImpl participantService;
	
	@MockBean
	private ParticipantService participantService1;
	
	@Mock
	private ReferenceNumberGeneratorServiceImpl referenceNumberGeneratorService;
	
	@MockBean
	private ReferenceNumberGeneratorService referenceNumberGeneratorService1;
	
	@Mock
	private ParticipantRepository participantRepository;
	
	@InjectMocks
	private ParticipantController participantController;
	
	private Participant participant;
	
	@BeforeEach
	public void setup(){
		participant = new Participant(1, "Joe Mason", new Date(1975-12-01),
				"555-555-5555", "12 park road", "JM-5555", false);
	}
	
	@Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(participantController).build();
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void getAllParticipantsListSizeTest() throws Exception{
		
		when(participantService.findAllParticipants(participant.getReferenceNumber()))
		.thenReturn(List.of(participant));
		
		ResponseEntity<List<Participant>> fetchedResponse = participantController
				.getAllParticipants(participant.getReferenceNumber());
		
		assertEquals(1, fetchedResponse.getBody().size());
	}
	
	@Test
	public void getAllParticipantsHttpStatusCodeTest() throws Exception{
		
		when(participantService.findAllParticipants(participant.getReferenceNumber()))
		.thenReturn(List.of(participant));
		
		ResponseEntity<List<Participant>> fetchedResponse = participantController
				.getAllParticipants(participant.getReferenceNumber());
		
		assertThat(fetchedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void testGetAllParticipants() throws Exception {
		List<Participant> participants = new ArrayList<>();
		participants.add(new Participant(1, "John Doe", new Date(1975-12-01),
				"123-456-7890", "123 Main St", "J0-7890", true));
		when(participantService1.findAllParticipants(null)).thenReturn(participants);

		mvc.perform(get("/newbeginningsapi/participants"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("John Doe")))
				.andExpect(jsonPath("$[0].address", is("123 Main St")))
				.andExpect(jsonPath("$[0].phoneNumber", is("123-456-7890")))
				.andExpect(jsonPath("$[0].published", is(true)));
	}
	
	@Test
	public void getParticipantByIdTest() {
		when(participantService.findParticipantById(participant.getId()))
		.thenReturn(Optional.of(participant));
		
		ResponseEntity<Participant> fetchedResponse = participantController
				.getParticipantById(participant.getId());
		
		assertEquals(participant.getName(), fetchedResponse.getBody().getName());
		
	}
	
	@Test
	public void testGetParticipantById() throws Exception {
		Participant participant = new Participant(1, "John Doe", new Date(1975-12-01),
				"123-456-7890", "123 Main St", "J0-7890", true);
		when(participantService1.findParticipantById(1L)).thenReturn(Optional.of(participant));

		mvc.perform(get("/newbeginningsapi/participants/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("John Doe")))
				 .andExpect(jsonPath("$.address", is("123 Main St")))
				 .andExpect(jsonPath("$.phoneNumber", is("123-456-7890")))
				.andExpect(jsonPath("$.published", is(true)));
	}
	
	@Test
	public void getParticipantByIdWithHttpStatusCodeTest() {
		when(participantService.findParticipantById(participant.getId()))
		.thenReturn(Optional.of(participant));
		
		ResponseEntity<Participant> fetchedResponse = participantController
				.getParticipantById(participant.getId());
		
		assertEquals(HttpStatus.OK, fetchedResponse.getStatusCode());
		
	}
	
	@Test
    public void testCreateParticipantSuccess() {
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAddress("123 Main St.");
        participant.setPhoneNumber("555-555-5555");

        when(referenceNumberGeneratorService.generateReferenceNumber(participant)).thenReturn("ABC123");

        Participant participantCreated = new Participant();
        participantCreated.setId(1L);
        participantCreated.setName(participant.getName());
        participantCreated.setAddress(participant.getAddress());
        participantCreated.setPhoneNumber(participant.getPhoneNumber());
        participantCreated.setPublished(false);
        participantCreated.setReferenceNumber("ABC123");

        when(participantService.saveParticipant(participant)).thenReturn(participantCreated);

        ResponseEntity<Participant> responseEntity = participantController.createParticipant(participant);

        verify(referenceNumberGeneratorService).generateReferenceNumber(participant);
        verify(participantService).saveParticipant(participant);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Participant responseParticipant = responseEntity.getBody();
        assertNotNull(responseParticipant);
        assertEquals(participantCreated.getId(), responseParticipant.getId());
        assertEquals(participantCreated.getName(), responseParticipant.getName());
        assertEquals(participantCreated.getAddress(), responseParticipant.getAddress());
        assertEquals(participantCreated.getPhoneNumber(), responseParticipant.getPhoneNumber());
        assertEquals(participantCreated.isPublished(), responseParticipant.isPublished());
        assertEquals(participantCreated.getReferenceNumber(), responseParticipant.getReferenceNumber());
    }
	@Test
    public void testCreateParticipantError() {
        Participant participant = new Participant();
        participant.setName("John Doe");
        participant.setAddress("123 Main St.");
        participant.setPhoneNumber("555-555-5555");

        when(referenceNumberGeneratorService.generateReferenceNumber(participant)).thenReturn("ABC123");

        when(participantService.saveParticipant(participant)).thenThrow(new RuntimeException());

        ResponseEntity<Participant> responseEntity = participantController.createParticipant(participant);

        verify(referenceNumberGeneratorService).generateReferenceNumber(participant);
        verify(participantService).saveParticipant(participant);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
	
	@Test
	public void createParticipantTest() {
		when(participantService.saveParticipant(any(Participant.class))).thenReturn(participant);
		
		ResponseEntity<Participant> createdParticipant = participantController
				.createParticipant(participant);
		
		assertEquals(participant.getReferenceNumber(), createdParticipant.getBody().getReferenceNumber());
	}
	
	@Test
	public void createParticipantWithHttpStatusCodeTest() {
		when(participantService.saveParticipant(any(Participant.class))).thenReturn(participant);
		
		ResponseEntity<Participant> createdParticipant = participantController
				.createParticipant(participant);
		
		assertEquals(HttpStatus.CREATED, createdParticipant.getStatusCode());
	}
	
   @Test
    public void testUpdateParticipantSuccess() {
        long id = 1;
        Participant existingParticipant = new Participant();
        existingParticipant.setId(id);
        existingParticipant.setName("John");
        existingParticipant.setAddress("123 Main St");
        existingParticipant.setPhoneNumber("555-5555");
        existingParticipant.setPublished(true);

        Participant updatedParticipant = new Participant();
        updatedParticipant.setId(id);
        updatedParticipant.setName("John Doe");
        updatedParticipant.setAddress("456 Elm St");
        updatedParticipant.setPhoneNumber("555-5555");
        updatedParticipant.setPublished(false);

        when(participantService.findParticipantById(id)).thenReturn(Optional.of(existingParticipant));
        when(participantService.saveParticipant(any(Participant.class))).thenReturn(updatedParticipant);

        
        ResponseEntity<Participant> response = participantController.updateParticipant(id, updatedParticipant);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedParticipant, response.getBody());
    }
   
   @Test
   public void testUpdateParticipantNotFound() {
       long id = 1;
       Participant updatedParticipant = new Participant();
       updatedParticipant.setId(id);
       updatedParticipant.setName("John Doe");
       updatedParticipant.setAddress("456 Elm St");
       updatedParticipant.setPhoneNumber("555-5555");
       updatedParticipant.setPublished(false);

       when(participantService.findParticipantById(id)).thenReturn(Optional.empty());

       ResponseEntity<Participant> response = participantController.updateParticipant(id, updatedParticipant);

       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertNull(response.getBody());
   }
	
	@Test
	public void updateParticipantTest() {
		given(participantService.saveParticipant(participant)).willReturn(participant);
		
		participant.setAddress("2 kingston road");
		participant.setPhoneNumber("555-666-5577");
		
		Participant updatedParticipant = participantService.saveParticipant(participant);
		
		assertThat(updatedParticipant.getAddress()).isEqualTo("2 kingston road");
		assertThat(updatedParticipant.getPhoneNumber()).isEqualTo("555-666-5577");
	}
	
    @Test
    public void deleteParticipantByIdTest() throws Exception {
        long id = 1L;
        HttpStatus expectedStatus = HttpStatus.OK;
        ResponseEntity<HttpStatus> expectedResponse = new ResponseEntity<>(expectedStatus);
        when(participantService1.deleteParticipantById(id)).thenReturn(expectedResponse);

        mvc.perform(delete("/newbeginningsapi/participants/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        verify(participantService1, times(1)).deleteParticipantById(id);
    }
    
    @Test
    public void deleteParticipantByIdFailureTest() throws Exception {
        long id = 1L;
        when(participantService1.deleteParticipantById(id)).thenThrow(new RuntimeException());

        mvc.perform(delete("/newbeginningsapi/participants/{id}", id))
                .andExpect(status().isInternalServerError())
                .andReturn();

        verify(participantService1, times(1)).deleteParticipantById(id);
    }
	
    @Test
    public void testFindParticipantByPublished() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(1, "John", new Date(1975-12-01),
        		"1234567890", "Test Address 1", "J0-7890", true));
        participants.add(new Participant(2, "Jane", new Date(1975-12-01),
        		"0987654321", "Test Address 2", "JA-4321", true));

        when(participantService.findParticipantByPublished()).thenReturn(participants);

        ResponseEntity<List<Participant>> response = participantController.findParticipantByPublished();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participants, response.getBody());
    }
}
