package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.UserVerfToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

/**
 * Created by SkandaBhargav on 11/30/16.
 */
@Repository
@Transactional
public class TokenDaoImpl implements TokenDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public UserVerfToken findToken(String token) {
        TypedQuery<UserVerfToken> query = em.createQuery("SELECT u from UserVerfToken u WHERE u.token = :value1", UserVerfToken.class).setParameter("value1", token);
        return query.getSingleResult();

    }

    @Override
    public void storeToken(UserVerfToken userVerfToken) {
        em.persist(userVerfToken);
    }
}
