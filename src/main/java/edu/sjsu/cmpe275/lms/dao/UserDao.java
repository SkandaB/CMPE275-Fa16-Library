/**
 * 
 */
package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	List<User> findAll();

    void updateUser(User user);
}
