package edu.sjsu.cmpe275.lms.dao;

/**
 * Created by Sagar on 12/2/2016.
 */
import edu.sjsu.cmpe275.lms.entity.Book;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Transactional
@Repository
public class BookDaoImpl implements BookDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    /**
     * Add a book into database
     *
     * @param book
     */
    @Override
    public boolean addBook(Book book) {
        entityManager.persist(book);
        return true;
    }

    @Override
    public boolean addBook(String isbn, String author, String title, String callnumber, String publisher, int year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image) {
        Book book = new Book(isbn, author, title, callnumber, publisher, year_of_publication, location, num_of_copies, current_status, keywords, image);
        entityManager.persist(book);
        return true;
    }

    /**
     * Return the book by isbn code
     *
     * @param isbn
     * @return book object
     */
    @Override
    public Book getBookByISBN(String isbn) {
        return entityManager.find(Book.class, isbn);
    }
}
