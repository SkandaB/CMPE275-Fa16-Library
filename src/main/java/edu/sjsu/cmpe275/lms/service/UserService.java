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

    /**
     * @return
     */
    List<User> listUsers();

    /**
     *
     * @param id
     * @return
     */
    public User findUser(Integer id);

    /**
     *
     * @param usermail
     * @return
     */
    public User findUserByEmail(String usermail);

    /**
     *
     * @param user
     * @param token
     */
    void createToken(User user, String token);

    /**
     *
     * @param token
     * @return
     */
    UserVerfToken getUserToken(String token);

    /**
     *
     * @param user
     */
    void saveValidatedUser(User user);
}
