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
import javax.persistence.Query;
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


    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.dao.UserDao#getUser(long)
     */
    @Override
    public User getUser(Integer id) {
        User user = em.find(User.class, id);
        return user;
    }
    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public User findUserByEmail(String usermail) {
        System.out.println("Email to Query:" + usermail);
        Query query = em.createQuery("select id from User u where u.useremail = ?");
        query.setParameter(1, usermail);
        List userIds = query.getResultList();
        // System.out.println("ID "+userIds.get(0));
        if (userIds.size() > 0) {
            User user = em.find(User.class, userIds.get(0));
            return user;
        }
        return null;
    }

}
