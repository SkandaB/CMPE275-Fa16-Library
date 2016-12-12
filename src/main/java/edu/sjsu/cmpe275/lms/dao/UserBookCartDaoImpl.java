package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.UserBookCart;
import edu.sjsu.cmpe275.lms.errors.Err;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class UserBookCartDaoImpl implements UserBookCartDao {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    /**
     * Adds the given book against the user to card
     *
     * @param ubc UserBookCart
     * @return Err false if add successful, true with error message if failed
     */
    @Override
    public Err addUserBookToCartIssue(UserBookCart ubc) {
        List<UserBookCart> ubcList = this.getUserCartIssue(ubc.getUser_id());
        if (ubcList.size() > 4) {
            return new Err(true, "You can only add 5 books to the cart at a time");
        }
        for (UserBookCart u : ubcList) {
            if (u.getBook_id() == ubc.getBook_id()) {
                return new Err(true, "Book already added to cart!");
            }
        }
        entityManager.persist(ubc);
        return new Err();
    }

    @Override
    public Err addUserBookToCartReturn(UserBookCart ubc) {
        List<UserBookCart> ubcList = this.getUserCartReturn(ubc.getUser_id());
        if (ubcList.size() > 9) {
            return new Err(true, "You can only add 10 books to the cart at a time");
        }
        for (UserBookCart u : ubcList) {
            if (u.getBook_id() == ubc.getBook_id()) {
                return new Err(true, "Book already added to cart!");
            }
        }
        entityManager.persist(ubc);
        return new Err();
    }

    /**
     * To get the cart for a user
     *
     * @param userid int userid
     * @return List of UserBookCart
     */
    @Override
    public List<UserBookCart> getUserCartIssue(int userid) {
        String query = "Select ubc From UserBookCart ubc WHERE ubc.user_id = :userid AND ubc.type_return = 0";
        Query q = entityManager.createQuery(query, UserBookCart.class);
        q.setParameter("userid", userid);
        return q.getResultList();
    }

    /**
     * To get the return cart for a user
     *
     * @param userid
     * @return
     */
    @Override
    public List<UserBookCart> getUserCartReturn(int userid) {
        String query = "Select ubc From UserBookCart ubc WHERE ubc.user_id = :userid AND ubc.type_return > 0";
        Query q = entityManager.createQuery(query, UserBookCart.class);
        q.setParameter("userid", userid);
        return q.getResultList();
    }

    /**
     * @param ubc
     * @return
     */
    @Override
    public boolean removeCartEntry(UserBookCart ubc) {
        entityManager.remove(ubc);
        return true;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Book> getUserBooksInCartIssue(int userId) {

        List<UserBookCart> ubcs = this.getUserCartIssue(userId);
        List<Book> books = new ArrayList<Book>();
        for (UserBookCart u : ubcs) {
            books.add(entityManager.find(Book.class, u.getBook_id()));

        }
        return books;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Book> getUserBooksInCartReturn(int userId) {
        List<UserBookCart> ubcs = this.getUserCartReturn(userId);
        List<Book> books = new ArrayList<Book>();
        for (UserBookCart u : ubcs) {
            books.add(entityManager.find(Book.class, u.getBook_id()));

        }
        return books;
    }

}
