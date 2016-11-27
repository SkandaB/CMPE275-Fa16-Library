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

/**
 * @author SkandaBhargav
 */
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

}
