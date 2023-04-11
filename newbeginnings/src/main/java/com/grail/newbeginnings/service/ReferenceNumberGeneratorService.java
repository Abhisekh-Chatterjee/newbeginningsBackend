package com.grail.newbeginnings.service;

import org.springframework.stereotype.Service;

import com.grail.newbeginnings.model.Participant;

@Service
public interface ReferenceNumberGeneratorService {
	
	String generateReferenceNumber(Participant participant);

}
