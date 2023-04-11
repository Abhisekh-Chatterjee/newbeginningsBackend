package com.grail.newbeginnings.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grail.newbeginnings.model.Participant;
import com.grail.newbeginnings.service.ParticipantService;
import com.grail.newbeginnings.service.ReferenceNumberGeneratorService;

@RestController
@RequestMapping("/newbeginningsapi")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class ParticipantController {

	@Autowired
	ParticipantService participantService;
	
	@Autowired
	ReferenceNumberGeneratorService referenceNumberGeneratorService;

	private static Logger log = LoggerFactory.getLogger(ParticipantController.class);

	@GetMapping("/participants")
	public ResponseEntity<List<Participant>> getAllParticipants(
			@RequestParam(required = false) String referenceNumber) {
		try {
			List<Participant> participants = participantService.findAllParticipants(referenceNumber);

			if (participants.isEmpty()) {
				return new ResponseEntity<List<Participant>>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(participants, HttpStatus.OK);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/participants/{id}")
	public ResponseEntity<Participant> getParticipantById(@PathVariable("id") @Min(1) long id){
		Optional<Participant> participant = participantService.findParticipantById(id);
		
		if(participant.isPresent()) {
			return new ResponseEntity<Participant>(participant.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Participant>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/participants")
	public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
		try {
			participant.setPublished(false);
			participant.setReferenceNumber(referenceNumberGeneratorService
					.generateReferenceNumber(participant));
			Participant participantCreated = participantService.saveParticipant(participant);
			return new ResponseEntity<Participant>(participantCreated, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/participants/{id}")
	public ResponseEntity<Participant> updateParticipant(@PathVariable("id") @Min(1) long id, 
			@RequestBody Participant participant){ 
		 Optional<Participant> participantFetched = participantService.findParticipantById(id);
		
		if(participantFetched.isPresent()) {
			
			Participant participantToBeSaved = participantFetched.get();
			
			if(null != participant.getName()) {
				participantToBeSaved.setName(participant.getName());
			}
			
			if(null != participant.getAddress()) {
				participantToBeSaved.setAddress(participant.getAddress());
			}
			
			if(null != participant.getPhoneNumber()) {
				participantToBeSaved.setPhoneNumber(participant.getPhoneNumber());
			}
			
			participantToBeSaved.setPublished(participant.isPublished());
			
			return new ResponseEntity<Participant>(participantService.saveParticipant(participantToBeSaved), 
					HttpStatus.OK);
		} else {
			return new ResponseEntity<Participant>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/participants/{id}")
	public ResponseEntity<HttpStatus> deleteParticipant(@PathVariable("id") long id) {
		try {
			return participantService.deleteParticipantById(id);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/participants")
	public ResponseEntity<HttpStatus> deleteAllParticipants() {
		try {
			return participantService.deleteAllParticipants();
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/participants/published")
	public ResponseEntity<List<Participant>> findParticipantByPublished() {
		try {
			List<Participant> participants = participantService.findParticipantByPublished();

			if (participants.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Participant>>(participants, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Participant>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
