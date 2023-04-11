package com.grail.newbeginnings.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grail.newbeginnings.model.Participant;

@ExtendWith(MockitoExtension.class)
class ReferenceNumberGeneratorServiceImplTest {

	@InjectMocks
	private ReferenceNumberGeneratorServiceImpl referenceNumberGeneratorServiceImpl;
	
	private Participant participant;
	
	@BeforeEach
    public void setup(){
 
		participant = new Participant(1, "Joe Mason", new Date(1975-12-01), "555-555-5555",
				"12 park road", "JM-5555", false);
    }
	
	@Test
	public void generateReferenceNumberTest() {
		
		assertEquals("JO-5555", referenceNumberGeneratorServiceImpl.generateReferenceNumber(participant));
		
	}
	
	@Test
	public void generateReferenceNumberTest_WithWrongOutput() {
		
		assertFalse("JO-5556".equals(referenceNumberGeneratorServiceImpl.generateReferenceNumber(participant)));
		
	}

}
