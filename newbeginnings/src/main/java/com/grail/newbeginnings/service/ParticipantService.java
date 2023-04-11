package com.grail.newbeginnings.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grail.newbeginnings.model.Participant;

@Service
public interface ParticipantService {
	
	List<Participant> findAllParticipants(String referenceNumber);
	
	Participant saveParticipant(Participant participant);

	Optional<Participant> findParticipantById(long id);
	
	ResponseEntity<HttpStatus> deleteParticipantById(long id);
	
	ResponseEntity<HttpStatus> deleteAllParticipants();
	
	List<Participant> findParticipantByPublished();
}
