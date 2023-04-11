package com.grail.newbeginnings.service;

import org.springframework.stereotype.Service;

import com.grail.newbeginnings.model.Participant;

@Service
public class ReferenceNumberGeneratorServiceImpl implements ReferenceNumberGeneratorService{
	
	public String generateReferenceNumber(Participant participant) {
		return participant.getName().substring(0, 2).toUpperCase() + "-" + 
				participant.getPhoneNumber().substring(participant.getPhoneNumber().length()-4, 
						participant.getPhoneNumber().length());
	}

}
