package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.UserBook;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserBookDao DAO for managing the user-book relationship.
 */
@Transactional
public interface UserBookDao {
    /**
     * Returns number of books the user is holding on a particular day
     *
     * @param userId
     * @return number of books issued by user on current date
     */
    int getUserDayBookCount(int userId);

    /**
     *
     * @param bookid
     * @return The boolean flag for the book id.
     */
    boolean exists(Integer bookid);

    /**
     * Removes the user book entity
     *
     * @param userBook
     * @return Status of the remove book wrt users.
     */
    boolean removeUserBook(UserBook userBook);
}
