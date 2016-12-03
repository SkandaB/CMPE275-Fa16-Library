package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.dao.TokenDao;
import edu.sjsu.cmpe275.lms.dao.UserDao;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserVerfToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    TokenDao tokenDao;

    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.service.UserService#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public User createUser(long sjsuid, String useremail, String password) {
        User user = new User();

        user.setSjsuid(sjsuid);
        user.setUseremail(useremail);
        user.setPassword(password);
        String role = (useremail.endsWith("@sjsu.edu")) ? "librarian" : "patron";
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
    
    @Override
    public User findUser(Integer sjsuid){
    	return userDao.getUser(sjsuid);
    }

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.service.UserService#getUser(long)
	 */
	

    @Override
    public void createToken(User user, String token) {
        UserVerfToken userVerfToken = new UserVerfToken(token, user);
        tokenDao.storeToken(userVerfToken);
    }

    @Override
    public UserVerfToken getUserToken(String token) {
        return tokenDao.findToken(token);
    }

    @Override
    public void saveValidatedUser(User user) {
        userDao.updateUser(user);
    }

}
