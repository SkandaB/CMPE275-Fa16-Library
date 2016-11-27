/**
 *
 */
package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.entity.User;

import java.util.List;

/**
 * @author SkandaBhargav
 */
public interface UserService {


    /**
     * @param sjsuid
     * @param useremail
     * @param password
     * @param role
     * @return
     */
    public User createUser(long sjsuid, String useremail, String password, String role);


    List<User> listUsers();

}
