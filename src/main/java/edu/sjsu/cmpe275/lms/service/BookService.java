/**
 * 
 */
package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @author dhanyaramesh
 *
 */
@EnableScheduling
public interface BookService {
	
    List<Book> listBooks();
	Book findBook(String isbn);
	String requestBook(Integer bookId,Integer userId) throws ParseException;
	Book findBookById(Integer bookId);
	List<Book> listBooksOfUser(Integer userId);
	String returnBook(Integer bookId,Integer userId);
	List<Book> searchBookbyUser(Book book);

    /**
     * @param id
     * @return
     */
    boolean deleteBookByID(Integer id);

    /**
     *
     * @param updatedbook
     * @param request
     * @return
     */
	Book updateBooks(Book updatedbook, HttpServletRequest request);

    /**
     *
     * @return
     */
    String getAvailableBookCount();

    /**
     *
     * @param isbn
     * @return
     */
	Book getBookByISBN(String isbn);

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
     * @return true if add successful, false if failed
     */
	boolean addBook(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image, User user);

    /**
     *
     * @return
     */
    List<Book> findAll();

    /**
     *
     * @return
     */
    List<LibUserBook> getAllLibUserBook();

    /**
     *
     * @param bookId
     * @param userId
     * @return
     * @throws ParseException
     */
    String renewBook(Integer bookId, Integer userId) throws ParseException;
}
