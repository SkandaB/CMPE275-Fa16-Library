package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;


@Entity
@Table(name="PHONE_DETAILS")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class PhoneEntity {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", length=8, unique = true, nullable = false)
	private Integer id;

	@Column(name = "NUMBER",unique=true,length=10)
	private String number;  // Note, phone numbers must be unique

	@Column(name = "DESCRIPTION")
	private String description;

	@Autowired
	@Embedded
	private AddressEntity address;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the address
	 */
	public AddressEntity getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressEntity address) {
		this.address = address;
	}

}
