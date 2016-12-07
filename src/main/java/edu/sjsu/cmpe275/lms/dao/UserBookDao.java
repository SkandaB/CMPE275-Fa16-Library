package edu.sjsu.cmpe275.lms.dao;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserBookDao {
    /**
     * Returns number of books the user is holding on a particular day
     * @param userId
     * @return number of books issued by user on current date
     */
    public int getUserDayBookCount(int userId);

    boolean exists(Integer bookid);
}
