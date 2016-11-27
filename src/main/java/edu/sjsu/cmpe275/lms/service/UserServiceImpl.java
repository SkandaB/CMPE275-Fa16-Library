package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.dao.UserDao;
import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.service.UserService#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public User createUser(long sjsuid, String useremail, String password, String role) {
        User user = new User();

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


}
