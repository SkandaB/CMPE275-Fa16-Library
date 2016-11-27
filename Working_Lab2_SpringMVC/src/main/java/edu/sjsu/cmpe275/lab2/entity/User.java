package edu.sjsu.cmpe275.lab2.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {

	public static final String ROLE_LIBRARIAN = "LIBRARIAN";
	public static final String ROLE_PATRON = "PATRON";
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name = "SJSUID", length = 50, unique = true, nullable = false)
	private long sjsuid;
	@Column(name = "USERNAME", nullable = false)
	private String username;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "ROLE")
	private String role;
	@Column(name = "enabled")
	private boolean enabled;

	public User() {
	}

	public User(String username, String password, String role, boolean enabled) {
		this.username = username;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
