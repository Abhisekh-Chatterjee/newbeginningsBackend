package com.grail.newbeginnings.model;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "participant")
public class Participant {
	
	@Id
	@SequenceGenerator(name = "participant_seq", sequenceName = "PARTICIPANT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participant_seq")
	private long id;
	
	@Column
	private String name;
	
	@Column
	@JsonSerialize(as = Date.class)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date dateOfBirth;
	
	@Column
	private String phoneNumber;
	
	@Column
	private String address;
	
	@Column
	private String referenceNumber;
	
	@Column
	private boolean published;

	public Participant() {
	}

	public Participant(long id, String name, Date dateOfBirth, String phoneNumber, String address,
			String referenceNumber, boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.referenceNumber = referenceNumber;
		this.published = published;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, dateOfBirth, id, name, phoneNumber, published, referenceNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participant other = (Participant) obj;
		return Objects.equals(address, other.address) && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& id == other.id && Objects.equals(name, other.name) && Objects.equals(phoneNumber, other.phoneNumber)
				&& published == other.published && Objects.equals(referenceNumber, other.referenceNumber);
	}
	

	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", phoneNumber="
				+ phoneNumber + ", address=" + address + ", referenceNumber=" + referenceNumber + ", published="
				+ published + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}
	
}
