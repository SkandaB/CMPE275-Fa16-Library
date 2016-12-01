/**
 *
 */
package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserVerfToken;

import java.util.List;

/**
 * @author SkandaBhargav
 */
public interface UserService {


    /**
     * @param sjsuid
     * @param useremail
     * @param password
     * @return
     */
    User createUser(long sjsuid, String useremail, String password);


    List<User> listUsers();

    void createToken(User user, String token);

    UserVerfToken getUserToken(String token);

    void saveValidatedUser(User user);
}
