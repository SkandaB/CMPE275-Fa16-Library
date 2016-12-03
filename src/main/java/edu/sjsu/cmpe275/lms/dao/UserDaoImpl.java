/**
 *
 */
package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Transactional
@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public User createUser(User uEntity) {
        em.persist(uEntity);
        return uEntity;
    }

    @Override
    public List<User> findAll() {
        List<User> users = (List<User>) em.createQuery("select u from User u", User.class).getResultList();
        return users;
    }

<<<<<<< HEAD
	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.dao.UserDao#getUser(long)
	 */
	@Override
	public User getUser(Integer sjsuid) {
		User user=em.find(User.class, sjsuid);
		return user;
	}
=======
    @Override
    public void updateUser(User user) {
        em.merge(user);
    }
>>>>>>> DB_Based_UserAuth

}
