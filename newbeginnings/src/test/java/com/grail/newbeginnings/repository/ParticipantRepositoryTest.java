package com.grail.newbeginnings.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.annotation.Rollback;

import com.grail.newbeginnings.model.Participant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ParticipantRepositoryTest {

	@Autowired
	private ParticipantRepository participantRepository;
	
	@Test
	@Order(1)
	@Rollback(value = false)
	public void saveParticipantTest() {
		Participant participant = new Participant(1, "Joe Mason", new Date(1975-12-01), 
				"555-555-5555","12 park road", "JM-5555", false);
		
		Participant participantSaved = participantRepository.save(participant);
		
		assertEquals(participant.getId(), participantSaved.getId());
	}
	
	@Test
	@Order(2)
	public void getParticipantTest() {
		Participant fetchedparticipant = participantRepository.findById(1L).get();
		
		assertEquals("Joe Mason", fetchedparticipant.getName());
	}
	
	@Test
	@Order(3)
	public void findByReferenceNumberContainingTest() {
		List<Participant> fetchedparticipants = participantRepository.findByReferenceNumberContaining("JM");
		
		assertEquals(1, fetchedparticipants.size());
	}
	
	@Test
	@Order(4)
	public void findByPublishedTest() {
		List<Participant> fetchedparticipants = participantRepository.findByPublished(false);
		
		assertThat(fetchedparticipants.size()).isGreaterThan(0);
	}
	
    @Test
    @Order(5)
    public void testFindByReferenceNumberContaining() {
        Participant participant1 = new Participant(1, "Joe Mason", new Date(1975-12-01), 
				"555-555-5555","12 park road", "JO-5555", false);
        participantRepository.save(participant1);
        
        Participant participant2 = new Participant(2, "Jane Smith", new Date(1976-12-01), 
        		"0987654321","456 Elm St", "JA-4321", false);
        participantRepository.save(participant2);
        
        List<Participant> result1 = participantRepository.findByReferenceNumberContaining("J");
        assertEquals(2, result1.size());
        assertTrue(result1.contains(participant1));
        assertTrue(result1.contains(participant2));
        
        List<Participant> result2 = participantRepository.findByReferenceNumberContaining("5555");
        assertEquals(1, result2.size());
        assertTrue(result2.contains(participant1));
        
        List<Participant> result3 = participantRepository.findByReferenceNumberContaining("XYZ");
        assertEquals(0, result3.size());
    }
    
    @Test
    @Order(6)
    public void testFindByPublishedShouldReturnPublishedParticipants() {
        Participant participant1 = new Participant(1, "Joe Mason", new Date(1975-12-01), 
				"555-555-5555","12 park road", "JO-5555", true);
        Participant participant2 = new Participant(2, "Jane Smith", new Date(1977-12-01), 
        		"456-789-0123","456 Oak St", "JA-0123", true);
        Participant participant3 = new Participant(3, "Bob Johnson", new Date(1976-12-01), 
        		"789-012-3456","789 Elm St", "JA-3456", false);
        participantRepository.saveAll(Arrays.asList(participant1, participant2, participant3));
        
        List<Participant> publishedParticipants = participantRepository.findByPublished(true);
        
        assertThat(publishedParticipants.size()).isEqualTo(2);
    }
	
    @Test
    @Order(7)
    public void testFindByPublishedShouldReturnEmptyListIfNoPublishedParticipants() {
    	Participant participant1 = new Participant(1, "Joe Mason", new Date(1975-12-01), 
				"555-555-5555","12 park road", "JO-5555", false);
        Participant participant2 = new Participant(2, "Jane Smith", new Date(1977-12-01), 
        		"456-789-0123","456 Oak St", "JA-0123", false);
        participantRepository.saveAll(Arrays.asList(participant1, participant2));
        
        List<Participant> publishedParticipants = participantRepository.findByPublished(true);
        
        assertThat(publishedParticipants.size()).isEqualTo(0);
    }
}
