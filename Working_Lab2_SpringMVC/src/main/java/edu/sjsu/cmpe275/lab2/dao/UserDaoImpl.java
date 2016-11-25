/**
 * 
 */
package edu.sjsu.cmpe275.lab2.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.entity.UserEntity;

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
	public UserEntity createUser(UserEntity uEntity) {
		em.persist(uEntity);
		return uEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#findById(int)
	 */
	@Override
	public UserEntity findById(int id) {
		UserEntity uEntity = em.find(UserEntity.class, id);
		//if(uEntity==null) return null;
		return uEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#deleteById(int)
	 */
	@Override
	public Boolean deleteById(int id) {
		UserEntity userEntity = em.find(UserEntity.class, id);
		em.remove(userEntity);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.sjsu.cmpe275.lab2.dao.UserDao#updateUser(edu.sjsu.cmpe275.lab2.entity
	 * .UserEntity)
	 */
	@Override
	public UserEntity updateUser(Integer id, UserEntity userEntity) {
		UserEntity uEntity = em.find(UserEntity.class, id);
		System.out.println("****************** Retrieved object " + uEntity.toString());
		System.out.println("****************** To be updated = " + userEntity.toString());
		uEntity.setFirstname(userEntity.getFirstname());
		uEntity.setLastname(userEntity.getLastname());
		uEntity.setTitle(userEntity.getTitle());
		uEntity.setAddress(userEntity.getAddress());
		
		em.merge(uEntity);
		System.out.println("After persist = " + em.find(UserEntity.class, id).toString());
		return uEntity;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.dao.UserDao#findALl()
	 */
	@Override
	public List<UserEntity> findALl() {
		List<UserEntity> users = (List<UserEntity>) em.createQuery("select u from UserEntity u", UserEntity.class).getResultList();
		return users;
	}

}
