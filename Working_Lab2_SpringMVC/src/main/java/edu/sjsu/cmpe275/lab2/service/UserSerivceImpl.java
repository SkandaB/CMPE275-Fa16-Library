package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.cmpe275.lab2.dao.UserDao;
import edu.sjsu.cmpe275.lab2.entity.AddressEntity;
import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.entity.UserEntity;

@Service
public class UserSerivceImpl implements UserSerivce {
	@Autowired
	UserDao userDao;

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserSerivce#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UserEntity createUser(String firstname, String lastname, String title, String city, String state, String street,
			String zip_code) {
		UserEntity userEntity = new UserEntity();
		AddressEntity addressEntity = new AddressEntity();

		userEntity.setFirstname(firstname);
		userEntity.setLastname(lastname);
		userEntity.setTitle(title);

		addressEntity.setCity(city);
		addressEntity.setState(state);
		addressEntity.setStreet(street);
		addressEntity.setZip(zip_code);

		userEntity.setAddress(addressEntity);

		// TODO Auto-generated method stub
		return userDao.createUser(userEntity);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserSerivce#findById(int)
	 */
	@Override
	public UserEntity findById(int id) {
		// TODO Auto-generated method stub
		return userDao.findById(id);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserSerivce#deleteById(int)
	 */
	@Override
	public Boolean deleteById(int id) {
		return userDao.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserSerivce#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UserEntity updateUser(Integer id, String firstname, String lastname, String title, String city, String state,
			String street, String zip_code) {
		UserEntity userEntity = new UserEntity();
		AddressEntity addressEntity = new AddressEntity();
		
		userEntity.setFirstname(firstname);
		userEntity.setLastname(lastname);
		userEntity.setTitle(title);

		addressEntity.setCity(city);
		addressEntity.setState(state);
		addressEntity.setStreet(street);
		addressEntity.setZip(zip_code);

		userEntity.setAddress(addressEntity);

		// TODO Auto-generated method stub
		return userDao.updateUser(id,userEntity);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserSerivce#findAll()
	 */
	@Override
	public List<UserEntity> findAll() {
		return userDao.findALl();
	}

}
