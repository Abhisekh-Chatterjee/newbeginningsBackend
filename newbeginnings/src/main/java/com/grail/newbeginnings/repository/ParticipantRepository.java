package com.grail.newbeginnings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.grail.newbeginnings.model.Participant;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
	
	@Query(value = "SELECT * FROM participant WHERE lower(reference_number) LIKE "
			+ "lower(concat('%', :referenceNumber,'%'))", nativeQuery = true)
	List<Participant> findByReferenceNumberContaining(@Param("referenceNumber") String referenceNumber);
	
	List<Participant> findByPublished(boolean published);

}
