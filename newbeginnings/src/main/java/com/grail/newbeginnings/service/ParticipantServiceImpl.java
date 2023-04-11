package com.grail.newbeginnings.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grail.newbeginnings.model.Participant;
import com.grail.newbeginnings.repository.ParticipantRepository;

@Service
public class ParticipantServiceImpl implements ParticipantService{
	
	@Autowired
	ParticipantRepository participantRepository;
	
	public List<Participant> findAllParticipants(String referenceNumber){
		List<Participant> participants= new ArrayList<Participant>();
		if(referenceNumber == null) {
			participantRepository.findAll().forEach(participants::add);
		} else {
			participantRepository.findByReferenceNumberContaining(referenceNumber).forEach(participants::add);
		}
		return participants;
	}
	
	public Participant saveParticipant(Participant participant) {
		return participantRepository.save(participant);
	}
	
	public Optional<Participant> findParticipantById(long id) {
		return participantRepository.findById(id);
	}
	
	public ResponseEntity<HttpStatus> deleteParticipantById(long id) {
		participantRepository.deleteById(id);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	
	public ResponseEntity<HttpStatus> deleteAllParticipants() {
		
		participantRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	public List<Participant> findParticipantByPublished() {
			return participantRepository.findByPublished(true);
			
	}

}
