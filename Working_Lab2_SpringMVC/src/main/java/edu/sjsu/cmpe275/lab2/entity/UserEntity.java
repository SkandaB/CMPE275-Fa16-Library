package edu.sjsu.cmpe275.lab2.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="USER_DETAILS")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class UserEntity {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "id", length=50, unique = true, nullable = false)
	private Integer id;

	@Column(name = "FIRST_NAME")
	private String firstname;

	@Column(name = "LAST_NAME")
	private String lastname;

	@Column(name = "TITLE")
	private String title;

	@Autowired
	@Embedded
	private AddressEntity address;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="USER_PHONE",
	joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
	inverseJoinColumns={@JoinColumn(name="PHONE_ID", referencedColumnName="ID")})
	private List<PhoneEntity> phones=new ArrayList<PhoneEntity>();


	public List<PhoneEntity> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneEntity> phones) {
		this.phones = phones;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public AddressEntity getAddress() {
		return address;
	}

	public void setAddress(AddressEntity addr) {
		this.address = addr;
	}

	public List<PhoneEntity> getPhone() {
		return this.phones;
	}

	public void setPhone(List<PhoneEntity> list) {
		this.phones = list;
	}

		

}
