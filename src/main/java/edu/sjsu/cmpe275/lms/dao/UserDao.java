/**
 *
 */
package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserDao {

    /**
     * @param uEntity
     * @return
     */
    User createUser(User uEntity);

    /**
     * @return
     */
    List<User> findAll();

    /**
     * @param id
     * @return
     */
    User getUser(Integer id);

    /**
     * @param usermail
     * @return
     */
    User findUserByEmail(String usermail);

    /**
     * @param user
     */
    void updateUser(User user);

    /**
     * Remove a user by ID
     * @param id
     * @return
     */
    boolean removeUser (Integer id);

}
