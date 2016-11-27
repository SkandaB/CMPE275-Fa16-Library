/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author SkandaBhargav
 *
 */

@Transactional
public interface PhoneDao {

/*

	*/
/**
	 * @param phoneEntity
	 * @return
 *//*

	PhoneEntity createPhone(PhoneEntity phoneEntity);

	*/
/**
	 * @param id
	 * @return
 *//*

	PhoneEntity findById(int id);

	*/
/**
	 * @param id
	 * @return
 *//*

	Boolean deleteById(int id);

	
	*/
/**
	 * @param id
	 * @param phoneEntity
	 * @param uids 
	 * @return
 *//*

	PhoneEntity updatePhone(Integer id, PhoneEntity phoneEntity, String uids);

	*/
/**
	 * @param id
	 * @return
 *//*

	List<User> retrchecked(Integer id);

	List<PhoneEntity> showAll();
*/

}
