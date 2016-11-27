package edu.sjsu.cmpe275.lab2.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {

	public static final String ROLE_LIBRARIAN = "LIBRARIAN";
	public static final String ROLE_PATRON = "PATRON";
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", length = 8, unique = true, nullable = false)
	private Integer id;
	@Column(name = "SJSUID", nullable = false)
	private long sjsuid;
	@Column(name = "USEREMAIL", nullable = false)
	private String useremail;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "ROLE")
	private String role;
	@Column(name = "enabled")
	private boolean enabled;

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
}
