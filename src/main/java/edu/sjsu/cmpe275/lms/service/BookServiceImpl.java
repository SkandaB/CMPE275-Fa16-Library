/**
 *
 */
package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @author dhanyaramesh
 */
@EnableScheduling
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookDao bookDao;

    @Autowired
    UserBookService userBookService;

    /**
     * @return
     */
    @Override
    public List<Book> listBooks() {
        return bookDao.findAll();
    }

    /**
     *
     * @param isbn
     * @return
     */
    @Override
    public Book findBook(String isbn) {
        // TODO Auto-generated method stub
        Book book = bookDao.getBookByISBN(isbn);
        return book;
    }

    /**
     *
     * @param bookId
     * @param userId
     * @return
     * @throws ParseException
     */
    @Override
    public String requestBook(Integer bookId, Integer userId) throws ParseException {
        // TODO Auto-generated method stub
        return bookDao.setBookRequest(bookId, userId);

    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Book findBookById(Integer bookId) {
        return bookDao.getBookbyId(bookId);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Book> listBooksOfUser(Integer userId) {

        List<Book> books = bookDao.getBookByUserId(userId);
        return books;
    }

    /**
     *
     * @param bookId
     * @param userId
     * @return
     */
    @Override
    public String returnBook(Integer bookId, Integer userId) {
        return bookDao.setBookReturn(bookId, userId);

    }

    /**
     * @param book
     * @return
     */
    @Override
    public List<Book> searchBookbyUser(Book book) {

        return bookDao.searchBook(book);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteBookByID(Integer id) {
        /*A book cannot be deleted if it’s checked out by a patron.*/
        if (!userBookService.checkIfEsxists(id)) {
            System.out.println("**************** Book does not exist, can delete safely. \n\r Now removing the waitlist entry for the same");

            return bookDao.deleteBookByID(id);
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getAvailableBookCount() {
        return bookDao.findCountAvailable();
    }

    /**
     *
     * @param updatedbook
     * @param request
     * @return
     */
    @Override
    public Book updateBooks(Book updatedbook, HttpServletRequest request) {
        return bookDao.updateBooks(updatedbook, request);
    }


    @Override
    public Book getBookByISBN(String isbn) {
        return bookDao.getBookByISBN(isbn);
    }

    /**
     * Add a book to database
     *
     * @param isbn                10 or 13 digit ISBN code, must be uniqur
     * @param author              Author of the book
     * @param title               Title of the book, must be unique
     * @param callnumber          Call Number
     * @param publisher           Publisher of the book
     * @param year_of_publication Year of publication
     * @param location            Location of the book in library
     * @param num_of_copies       Number of copies
     * @param current_status      Current Status
     * @param keywords            Keywords
     * @param image               Bytes as image
     * @param user
     * @return true if add successful, false if failed
     */
    @Override
    public boolean addBook(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image, User user) {
        return bookDao.addBook(isbn, author, title, callnumber, publisher, year_of_publication, location, num_of_copies, current_status, keywords, image, user);
    }

    /**
     *
     * @return
     */
    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    /**
     *
     * @return
     */
    @Override
    public List<LibUserBook> getAllLibUserBook() {
        return bookDao.getAllLibUserBook();
    }

    /**
     *
     * @param bookId
     * @param userId
     * @return
     * @throws ParseException
     */
    @Override
    public String renewBook(Integer bookId, Integer userId) throws ParseException {
        return bookDao.setBookRenew(bookId, userId);
    }
}
