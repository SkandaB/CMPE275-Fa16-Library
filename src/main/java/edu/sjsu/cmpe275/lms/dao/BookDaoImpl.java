package edu.sjsu.cmpe275.lms.dao;

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
    private EntityManager em;

    /**
     * Add a book into database
     *
     * @param book
     */
    @Override
    public boolean addBook(Book book) {
        em.persist(book);
        return true;
    }
}
