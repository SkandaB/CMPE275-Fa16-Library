/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SkandaBhargav
 *
 */
@Transactional
@Repository
public class PhoneDaoImpl implements PhoneDao {
/*
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	*/
/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.PhoneDao#createPhone(edu.sjsu.cmpe275.lab2.entity.PhoneEntity)
	 *//*

	@Override
	public PhoneEntity createPhone(PhoneEntity phoneEntity) {
		em.persist(phoneEntity);
		return phoneEntity;
	}

	*/
/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.PhoneDao#findById(int)
	 *//*

	@Override
	public PhoneEntity findById(int id) {
		PhoneEntity pEntity = em.find(PhoneEntity.class, id);
		return pEntity;
	}

	*/
/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.PhoneDao#deleteById(int)
	 *//*

	@Override
	public Boolean deleteById(int id) {
		PhoneEntity phoneEntity = em.find(PhoneEntity.class, id);
		em.remove(phoneEntity);
		return true;
	}

	*/
/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.PhoneDao#updatePhone(java.lang.Integer, edu.sjsu.cmpe275.lab2.entity.PhoneEntity)
	 *//*

	@Override
	public PhoneEntity updatePhone(Integer id, PhoneEntity phoneEntity, String uids) {
		PhoneEntity pEntity = em.find(PhoneEntity.class,id);
		List<String> users = Arrays.asList(uids.split(","));


		pEntity.setNumber(phoneEntity.getNumber());
		pEntity.setDescription(phoneEntity.getDescription());
		pEntity.setAddress(phoneEntity.getAddress());

		for (String string : users) {
			System.out.println(string);
			pEntity.getUsers().add(em.find(User.class, Integer.parseInt(string)));
			em.find(User.class, Integer.parseInt(string)).getPhones().add(pEntity);
		}


		em.merge(pEntity);
		return pEntity;
	}
*/


/*

	*/
/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.PhoneDao#retrchecked(java.lang.Integer)
	 *//*

	@Override
	public List<User> retrchecked(Integer id) {
List<User> users = em.find(PhoneEntity.class, id).getUsers();
		return users;
	}

	@Override
	public List<PhoneEntity> showAll() {
		return em.createQuery("Select p from PhoneEntity p").getResultList();
	}
*/

}
