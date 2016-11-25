/**
 * 
 */
package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import edu.sjsu.cmpe275.lab2.entity.*;

/**
 * @author SkandaBhargav
 *
 */
public interface UserSerivce {

	
	/**
	 * @param firstname
	 * @param lastname
	 * @param title
	 * @param city
	 * @param state
	 * @param street
	 * @param zip
	 * @return
	 */
	public UserEntity createUser(String firstname, String lastname, String title, String city, String state, String street,
			String zip);
	
	/**
	 * @param id
	 * @return
	 */
	public  UserEntity findById(int id);
	
	/**
	 * @param id
	 * @return 
	 */
	public Boolean deleteById(int id);
	
	/**
	 * @param firstname
	 * @param lastname
	 * @param title
	 * @param city
	 * @param state
	 * @param street
	 * @param zip_code
	 * @return
	 */
	public UserEntity updateUser(Integer id,String firstname, String lastname, String title, String city, String state,
			String street, String zip_code);

	/**
	 * @return
	 */
	public List<UserEntity> findAll();
}
