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

    boolean addBook(String isbn, String author, String title, String callnumber, String publisher, int year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image);
}
