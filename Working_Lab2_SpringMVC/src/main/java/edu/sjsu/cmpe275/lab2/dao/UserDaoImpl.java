/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import edu.sjsu.cmpe275.lab2.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

/**
 * @author SkandaBhargav
 *
 */
@Transactional
@Repository
public class UserDaoImpl implements UserDao {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#createUser(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public User createUser(User uEntity) {
		em.persist(uEntity);
		return uEntity;
	}
/*

	*/
/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#findById(int)
	 *//*

	@Override
	public User findById(int id) {
		User uEntity = em.find(User.class, id);
		//if(uEntity==null) return null;
		return uEntity;
	}

	*/
/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#deleteById(int)
	 *//*

	@Override
	public Boolean deleteById(int id) {
		User user = em.find(User.class, id);
		em.remove(user);
		return true;
	}

	*/
/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.sjsu.cmpe275.lab2.dao.UserDao#updateUser(edu.sjsu.cmpe275.lab2.entity
	 * .User)
	 *//*

	@Override
	public User updateUser(Integer id, User user) {
		User uEntity = em.find(User.class, id);
		System.out.println("****************** Retrieved object " + uEntity.toString());
		System.out.println("****************** To be updated = " + user.toString());
		uEntity.setFirstname(user.getFirstname());
		uEntity.setLastname(user.getLastname());
		uEntity.setTitle(user.getTitle());
		uEntity.setAddress(user.getAddress());
		
		em.merge(uEntity);
		System.out.println("After persist = " + em.find(User.class, id).toString());
		return uEntity;
	}

	*/
@Override
public List<User> findAll() {
		List<User> users = (List<User>) em.createQuery("select u from User u", User.class).getResultList();
		return users;
	}

}
