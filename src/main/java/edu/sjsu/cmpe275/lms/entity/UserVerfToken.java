package edu.sjsu.cmpe275.lms.entity;

import javax.persistence.*;

@Entity
public class UserVerfToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    /**
     * Default constructor.
     */
    public UserVerfToken() {
        super();
    }

    /**
     * @param token
     * @param user
     */
    public UserVerfToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    /**
     *
     * @return
     */

    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
