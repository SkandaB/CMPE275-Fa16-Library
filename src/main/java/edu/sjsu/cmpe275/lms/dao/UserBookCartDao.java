package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.UserBookCart;
import edu.sjsu.cmpe275.lms.errors.Err;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional

public interface UserBookCartDao {

    /**
     * Adds the given book against the user to cart for issue
     *
     * @param ubc UserBookCart
     * @return true if add successful, false if failed
     */
    Err addUserBookToCartIssue(UserBookCart ubc);

    /**
     * Adds the given book against the user to cart for return
     * @param ubc
     * @return
     */
    Err addUserBookToCartReturn(UserBookCart ubc);

    /**
     * To get the issue cart for a user
     *
     * @param userid int userid
     * @return List of UserBookCart
     */
    List<UserBookCart> getUserCartIssue(int userid);

    /**
     * To get the return cart for a user
     *
     * @param userid
     * @return List of the user books cart.
     */
    List<UserBookCart> getUserCartReturn(int userid);

    /**
     *
     * @param ubc
     * @return Delete status of the cart.
     */
    boolean removeCartEntry(UserBookCart ubc);

    /**
     *
     * @param userId
     * @return The list of the books wrt to users which are in cart.
     */
    List<Book> getUserBooksInCartIssue(int userId);

    /**
     * @param userId
     * @return List of the user books in the cart.
     */
    List<Book> getUserBooksInCartReturn(int userId);
}