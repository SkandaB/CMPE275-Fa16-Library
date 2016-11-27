package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.dao.UserDao;
import edu.sjsu.cmpe275.lab2.entity.AddressEntity;
import edu.sjsu.cmpe275.lab2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserService#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User createUser(long sjsuid, String useremail, String password, String role) {
		User user = new User();
		AddressEntity addressEntity = new AddressEntity();

		user.setSjsuid(sjsuid);
		user.setUseremail(useremail);
		user.setPassword(password);
		user.setRole(role);


		// TODO Auto-generated method stub
		return userDao.createUser(user);
	}

	/**
	 * @return
	 */
	@Override
	public List<User> listUsers() {
		return userDao.findAll();
	}
/*
	*//* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserService#findById(int)
	 *//*
	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		return userDao.findById(id);
	}

	*//* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserService#deleteById(int)
	 *//*
	@Override
	public Boolean deleteById(int id) {
		return userDao.deleteById(id);
	}

	*//* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserService#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 *//*
	@Override
	public User updateUser(Integer id, String firstname, String lastname, String title, String city, String state,
                           String street, String zip_code) {
		User user = new User();
		AddressEntity addressEntity = new AddressEntity();
		
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setTitle(title);

		addressEntity.setCity(city);
		addressEntity.setState(state);
		addressEntity.setStreet(street);
		addressEntity.setZip(zip_code);

		user.setAddress(addressEntity);

		// TODO Auto-generated method stub
		return userDao.updateUser(id, user);
	}

	*//* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.UserService#findAll()
	 *//*
	@Override
	public List<User> findAll() {
		return userDao.findALl();
	}*/

}
