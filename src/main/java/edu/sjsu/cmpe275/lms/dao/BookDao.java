package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookDao {
    /**
     * Add a book into database
     *
     * @param book
     */
    boolean addBook(Book book);
}
