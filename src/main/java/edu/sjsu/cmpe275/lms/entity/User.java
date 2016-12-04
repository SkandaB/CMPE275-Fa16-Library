package edu.sjsu.cmpe275.lms.entity;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"SJSUID", "ROLE"}))
public class User {

	public static final String ROLE_LIBRARIAN = "LIBRARIAN";
	public static final String ROLE_PATRON = "PATRON";
	@OneToMany(mappedBy = "user")
	List<UserBook> currentBooks = new ArrayList<UserBook>();
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", length = 8, unique = true, nullable = false)
	private Integer id;
	@Range(min = 100000, max = 999999, message = "SJSU ID is of fixed size 6 digits")
	@Column(name = "SJSUID", nullable = false)
	private Long sjsuid;
	@Email(message = "Please enter a valid email")
	@Column(name = "USEREMAIL", nullable = false, unique = true)
	@NotEmpty(message = "Email cannot be empty")
	private String useremail;
	@NotEmpty(message = "Password cannot be empty")
	@Size(min = 6, max = 15, message = "Your password must between 6 and 15 characters")
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "ROLE")
	private String role;
	@Column(name = "ENABLED")
	private boolean enabled;

	public User() {
		this.enabled = false;
	}

	public User(String useremail, String password, String role, boolean enabled) {
		this.useremail = useremail;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getSjsuid() {
		return sjsuid;
	}

	public void setSjsuid(Long sjsuid) {
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


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", sjsuid=" + sjsuid +
				", useremail='" + useremail + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				", enabled=" + enabled +
				'}';
	}
}
