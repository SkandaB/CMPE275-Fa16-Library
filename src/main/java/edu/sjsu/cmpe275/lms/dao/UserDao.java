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
	List<User> findAll();
<<<<<<< HEAD
	public User getUser(Integer sjsuid);
=======

    void updateUser(User user);
>>>>>>> DB_Based_UserAuth
}
