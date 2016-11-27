/**
 * 
 */
package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.User;

import java.util.List;

/**
 * @author SkandaBhargav
 *
 */
public interface UserService {

	
	/**
	 * @param sjsuid
	 * @param useremail
	 * @param password
	 * @param role
	 * @return
	 */
	public User createUser(long sjsuid, String useremail, String password, String role);
/*
	*//**
	 * @param id
	 * @return
	 *//*
	public User findById(int id);
	
	*//**
	 * @param id
	 * @return
	 *//*
	public Boolean deleteById(int id);
	
	*//**
	 * @param firstname
	 * @param lastname
	 * @param title
	 * @param city
	 * @param state
	 * @param street
	 * @param zip_code
	 * @return
	 *//*
	public User updateUser(Integer id, String firstname, String lastname, String title, String city, String state,
                           String street, String zip_code);

	*//**
	 * @return
	 *//*
	public List<User> findAll();
	*/


	List<User> listUsers();

}
