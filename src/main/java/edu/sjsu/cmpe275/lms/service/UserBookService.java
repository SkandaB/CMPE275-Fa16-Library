package edu.sjsu.cmpe275.lms.service;

public interface UserBookService {

    /**
     * Returns number of books the user is holding on a particular day
     *
     * @param userId
     * @return number of books issued by user on current date
     */
    int getUserDayBookCount (int userId);

    boolean checkIfEsxists(Integer bookid);

}
