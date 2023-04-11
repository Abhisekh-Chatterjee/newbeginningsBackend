package com.grail.newbeginnings.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.grail.newbeginnings.model.Participant;
import com.grail.newbeginnings.repository.ParticipantRepository;


@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {
	
	
	@Mock
	private ParticipantRepository participantRepository;
	 
	@Autowired
	MockMvc mvc;
	
	@InjectMocks
	private ParticipantServiceImpl participantService;
	
	@MockBean
	private ParticipantService participantService1;
	
	private Participant participant;
	
	@BeforeEach
    public void setup(){
 
		participant = new Participant(1, "Joe Mason", new Date(1975-12-01), "555-555-5555",
				"12 park road", "JM-5555", false);
    }
	
	@Test
	public void testFindAllParticipants() {
	    Participant participant1 = new Participant();
	    participant1.setId(1L);
	    participant1.setName("Alice");
	    participant1.setAddress("123 Main St");
	    participant1.setPhoneNumber("555-1234");
	    participant1.setPublished(true);
	    participant1.setReferenceNumber("ABC123");

	    Participant participant2 = new Participant();
	    participant2.setId(2L);
	    participant2.setName("Bob");
	    participant2.setAddress("456 Elm St");
	    participant2.setPhoneNumber("555-5678");
	    participant2.setPublished(false);
	    participant2.setReferenceNumber("DEF456");

	    List<Participant> mockParticipants = Arrays.asList(participant1, participant2);

	    when(participantRepository.findAll()).thenReturn(mockParticipants);
	    when(participantRepository.findByReferenceNumberContaining("ABC")).thenReturn(Arrays.asList(participant1));

	    List<Participant> allParticipants = participantService.findAllParticipants(null);
	    assertEquals(2, allParticipants.size());
	    assertTrue(allParticipants.contains(participant1));
	    assertTrue(allParticipants.contains(participant2));

	    List<Participant> matchingParticipants = participantService.findAllParticipants("ABC");
	    assertEquals(1, matchingParticipants.size());
	    assertEquals(participant1, matchingParticipants.get(0));
	}

	
	@Test
	public void findAllParticipantsTest() {
		
		given(participantRepository.findByReferenceNumberContaining(participant.getReferenceNumber()))
		.willReturn(List.of(participant));
		
		List<Participant> participantFetched = participantService
				.findAllParticipants(participant.getReferenceNumber());
		
		assertThat(participantFetched.size()).isEqualTo(1);
	}
	
	
	@Test
	public void saveParticipantTest() {
		
		given(participantRepository.save(participant)).willReturn(participant);
		
		Participant participantSaved = participantService.saveParticipant(participant);
		
		assertEquals(participant.getId(), participantSaved.getId());
	}
	
   @Test
    public void testFindParticipantById() {
        Participant participant = new Participant();
        participant.setId(1L);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        
        Optional<Participant> actualParticipant = participantService.findParticipantById(1L);
        assertTrue(actualParticipant.isPresent());
        assertEquals(participant, actualParticipant.get());
    }
	
	@Test
	public void findParticipantByIdTest() {
		
		given(participantRepository.findById(1L)).willReturn(Optional.of(participant));
		
		Participant participantFetched = participantService
				.findParticipantById(participant.getId()).get();
		
		assertEquals(participantFetched.getId(), participant.getId());
	}
	
   @Test
    public void testFindParticipantByIdNotFound() {
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<Participant> actualParticipant = participantService.findParticipantById(1L);
        assertFalse(actualParticipant.isPresent());
    }
   
   @Test
   public void testDeleteParticipantById_Successful() {
       long id = 1L;
       doNothing().when(participantRepository).deleteById(id);

       ResponseEntity<HttpStatus> responseEntity = participantService.deleteParticipantById(id);

       assertNotNull(responseEntity);
       assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
   }
	
	@Test
	public void deleteParticipantByIdTest() {
		
		willDoNothing().given(participantRepository).deleteById(1L);

        participantService.deleteParticipantById(1L);

        verify(participantRepository, times(1)).deleteById(1L);
	}
	
    @Test
    public void deleteParticipantById_shouldReturnNoContent() {
        long id = 1L;
        
        ResponseEntity<HttpStatus> responseEntity = participantService.deleteParticipantById(id);
        
        verify(participantRepository).deleteById(id);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
	
	@Test
	public void deleteAllTest() {
		
		willDoNothing().given(participantRepository).deleteAll();

        participantService.deleteAllParticipants();

        verify(participantRepository, times(1)).deleteAll();
	}
	
	@Test
	public void findByPublishedTest() {
		
		given(participantRepository.findByPublished(true))
		.willReturn(List.of(participant));
		
		List<Participant> participantFetched = participantService
				.findParticipantByPublished();
		
		assertThat(participantFetched.size()).isEqualTo(1);
	}

}
