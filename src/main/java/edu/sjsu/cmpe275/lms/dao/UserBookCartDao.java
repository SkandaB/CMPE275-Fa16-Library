package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.UserBookCart;
import edu.sjsu.cmpe275.lms.errors.Err;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserBookCartDao {

    /**
     * Adds the given book against the user to card
     * @param ubc UserBookCart
     * @return true if add successful, false if failed
     */
    Err addUserBookToCart (UserBookCart ubc);

    /**
     * To get the cart for a user
     * @param userid int userid
     * @return List of UserBookCart
     */
    List<UserBookCart> getUserCart (int userid);

    boolean removeCartEntry (UserBookCart ubc);
}