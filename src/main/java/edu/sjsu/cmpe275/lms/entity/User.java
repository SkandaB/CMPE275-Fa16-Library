package edu.sjsu.cmpe275.lms.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {

	public static final String ROLE_LIBRARIAN = "LIBRARIAN";
	public static final String ROLE_PATRON = "PATRON";
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "USER_ID", length = 8, unique = true, nullable = false)
	private Integer userId;
		@Column(name = "SJSUID", nullable = false,unique = true)
	private long sjsuid;
	@Column(name = "USEREMAIL", nullable = false)
	private String useremail;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "ROLE")
	private String role;
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToMany(mappedBy="user")
	List<UserBook> currentBooks = new ArrayList<UserBook>();

	public User() {
	}

	public User(String useremail, String password, String role, boolean enabled) {
		this.useremail = useremail;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
	}

	public long getSjsuid() {
		return sjsuid;
	}

	public void setSjsuid(long sjsuid) {
		this.sjsuid = sjsuid;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String username) {
		this.useremail = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the currentBooks
	 */
	public List<UserBook> getCurrentBooks() {
		return currentBooks;
	}

	/**
	 * @param currentBooks the currentBooks to set
	 */
	public void setCurrentBooks(List<UserBook> currentBooks) {
		this.currentBooks = currentBooks;
	}


}
