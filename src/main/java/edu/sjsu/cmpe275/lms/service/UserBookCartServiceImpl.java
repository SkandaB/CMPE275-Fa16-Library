package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.dao.UserBookCartDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.UserBookCart;
import edu.sjsu.cmpe275.lms.errors.Err;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookCartServiceImpl implements UserBookCartService {

    @Autowired
    UserBookCartDao userBookCartDao;

    /**
     * Adds the given book against the user to card
     *
     * @param ubc UserBookCart
     * @return true if add successful, false if failed
     */
    @Override
    public Err addUserBookToCart(UserBookCart ubc) {
        if (ubc.getType_return() > 0) {
            return userBookCartDao.addUserBookToCartReturn(ubc);
        } else {
            return userBookCartDao.addUserBookToCartIssue(ubc);
        }
    }

    /**
     * To get the cart for a user
     *
     * @param userid int userid
     * @return List of UserBookCart
     */
    @Override
    public List<UserBookCart> getUserCart(int userid, boolean isTypeReturn) {
        if (isTypeReturn) {
            return userBookCartDao.getUserCartReturn(userid);
        } else {
            return userBookCartDao.getUserCartIssue(userid);
        }

    }

    /**
     * Clears the books for user from cart
     *
     * @param userid
     */
    @Override
    public void clearUserCart(int userid, boolean isTypeReturn) {
        List<UserBookCart> userBookCartList;
        if (isTypeReturn) {
            userBookCartList = userBookCartDao.getUserCartReturn(userid);
        } else {
            userBookCartList = userBookCartDao.getUserCartIssue(userid);
        }
        for (UserBookCart u : userBookCartList) {
            userBookCartDao.removeCartEntry(u);
        }
    }

    @Override
    public List<Book> getUserBooks(int userId, boolean isTypeReturn) {
        List<Book> books;
        if (isTypeReturn) {
            books = userBookCartDao.getUserBooksInCartReturn(userId);
        } else {
            books = userBookCartDao.getUserBooksInCartIssue(userId);
        }
        return books;

    }
}
