/**
 * 
 */
package edu.sjsu.cmpe275.lab2.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.dao.PhoneDao;
import edu.sjsu.cmpe275.lab2.entity.AddressEntity;
import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.entity.UserEntity;
/**
 * @author SkandaBhargav
 *
 */
@Service()
public class PhoneServiceImpl implements PhoneService {
	@Autowired
	PhoneDao phoneDao;


	@Override
	public PhoneEntity createUser(String number, String description, String city, String state, String street,
			String zip_code) {
		PhoneEntity phoneEntity = new PhoneEntity();
		AddressEntity addressEntity = new AddressEntity();
		
		phoneEntity.setNumber(number);
		phoneEntity.setDescription(description);
		
		addressEntity.setCity(city);
		addressEntity.setState(state);
		addressEntity.setStreet(street);
		addressEntity.setZip(zip_code);
		
		phoneEntity.setAddress(addressEntity);
		
		return phoneDao.createPhone(phoneEntity);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.PhoneService#findById(int)
	 */
	@Override
	public PhoneEntity findById(int id) {
		// TODO Auto-generated method stub
		return phoneDao.findById(id);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.PhoneService#deleteById(int)
	 */
	@Override
	public boolean deleteById(int id) {
		return phoneDao.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.PhoneService#updatePhone(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PhoneEntity updatePhone(Integer id, String number, String description, String city, String state,
			String street, String zip_code, String uids) {
		PhoneEntity phoneEntity = new PhoneEntity();
		AddressEntity addressEntity = new AddressEntity();
		
		phoneEntity.setNumber(number);
		phoneEntity.setDescription(description);
		
		addressEntity.setCity(city);
		addressEntity.setState(state);
		addressEntity.setStreet(street);
		addressEntity.setZip(zip_code);
		
		phoneEntity.setAddress(addressEntity);
		
		return phoneDao.updatePhone(id,phoneEntity,uids);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lab2.service.PhoneService#retrieveUsers(java.lang.Integer)
	 */
	@Override
	public List<UserEntity> retrieveUsers(Integer id) {
		return phoneDao.retrchecked(id);
	} 

}