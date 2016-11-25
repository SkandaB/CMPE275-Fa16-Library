/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entity.UserEntity;

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
	UserEntity createUser(UserEntity uEntity);

	/**
	 * @param id
	 * @return
	 */
	UserEntity findById(int id);

	/**
	 * @param id
	 */
	Boolean deleteById(int id);

	/**
	 * @param userEntity
	 * @return
	 */
	UserEntity updateUser(Integer id, UserEntity userEntity);

	/**
	 * @return
	 */
	List<UserEntity> findALl();
}
