package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.dao.UserBookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    UserBookDao userBookDao;
    /**
     * Returns number of books the user is holding on a particular day
     *
     * @param userId
     * @return number of books issued by user on current date
     */
    @Override
    public int getUserDayBookCount(int userId) {
        return userBookDao.getUserDayBookCount(userId);
    }

    /**
     * @param bookid
     * @return
     */
    @Override
    public boolean checkIfEsxists(Integer bookid) {
        return userBookDao.exists(bookid);
    }


}
