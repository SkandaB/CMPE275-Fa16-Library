/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import edu.sjsu.cmpe275.lab2.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SkandaBhargav
 *
 */
@Transactional
public interface UserDao {

	/**
	 * @param uEntity
	 * @return
	 */
	User createUser(User uEntity);
/*
	*//**
	 * @param id
	 * @return
	 *//*
	User findById(int id);

	*//**
	 * @param id
	 *//*
	Boolean deleteById(int id);

	*//**
	 * @param user
	 * @return
	 *//*
	User updateUser(Integer id, User user);

	*//**
	 * @return
	 *//*
	List<User> findALl();*/
}
