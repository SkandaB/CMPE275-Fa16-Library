/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entity.AddressEntity;
import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.entity.UserEntity;

/**
 * @author SkandaBhargav
 *
 */

@Transactional
public interface PhoneDao {

	/**
	 * @param addressEntity
	 * @return
	 */
	PhoneEntity createPhone(PhoneEntity phoneEntity);

	/**
	 * @param id
	 * @return
	 */
	PhoneEntity findById(int id);

	/**
	 * @param id
	 * @return
	 */
	Boolean deleteById(int id);

	
	/**
	 * @param id
	 * @param phoneEntity
	 * @param uids 
	 * @return
	 */
	PhoneEntity updatePhone(Integer id, PhoneEntity phoneEntity, String uids);

	/**
	 * @param id
	 * @return
	 */
	List<UserEntity> retrchecked(Integer id);
	

}
