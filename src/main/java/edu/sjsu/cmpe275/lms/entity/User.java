package edu.sjsu.cmpe275.lms.entity;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = {"SJSUID", "ROLE"}))
public class User {

    public static final String ROLE_LIBRARIAN = "LIBRARIAN";
    public static final String ROLE_PATRON = "PATRON";
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    List<UserBook> currentBooks = new ArrayList<UserBook>();
    // Added for search, add and update
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    List<LibUserBook> addUpdateList = new ArrayList<LibUserBook>();
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     *
     */
    public User() {
        this.enabled = false;
    }

    /**
     * @param useremail
     * @param password
     * @param role
     * @param enabled
     */
    public User(String useremail, String password, String role, boolean enabled) {
        this.useremail = useremail;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    /**
     *
     * @param sjsuid
     * @param useremail
     * @param password
     * @param role
     * @param enabled
     */
    public User(long sjsuid, String useremail, String password, String role, boolean enabled) {
        this.sjsuid = sjsuid;
        this.useremail = useremail;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }
    // Add End

    /**
     *
     * @return THe Library user books list
     */
    public List<LibUserBook> getAddUpdateList() {
        return addUpdateList;
    }

    /**
     *
     * @param addUpdateList
     */
    public void setAddUpdateList(List<LibUserBook> addUpdateList) {
        this.addUpdateList = addUpdateList;
    }

    /**
     *
     * @return The ID of the user
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Long getSjsuid() {
        return sjsuid;
    }

    /**
     *
     * @param sjsuid
     */
    public void setSjsuid(Long sjsuid) {
        this.sjsuid = sjsuid;
    }

    /**
     *
     * @return
     */
    public String getUseremail() {
        return useremail;
    }

    /**
     *
     * @param username
     */
    public void setUseremail(String username) {
        this.useremail = username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @param enabled
     */
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

    /**
     *
     * @return The string representation of theuser object.
     */
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
