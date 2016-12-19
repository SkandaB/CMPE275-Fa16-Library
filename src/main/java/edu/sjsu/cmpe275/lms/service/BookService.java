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
	/**
	 * @return List of all books in the library
	 */
	List<Book> listBooks();

	/**
	 *
	 * @param isbn
	 * @return the book with given isbh
	 */
	Book findBook(String isbn);

	/**
	 *
	 * @param bookId
	 * @param userId
	 * @return the resulf of requesting a book by a user
	 * @throws ParseException
	 */
	String requestBook(Integer bookId,Integer userId) throws ParseException;

	/**
	 * @param bookId
	 * @return book with given book_id
	 */
	Book findBookById(Integer bookId);

	/**
	 * @param userId
	 * @return books checkoued by user with userId
	 */
	List<Book> listBooksOfUser(Integer userId);

	/**
	 *
	 * @param bookId
	 * @param userId
	 * @return status after return book by user
	 */
	String returnBook(Integer bookId,Integer userId);

	/**
	 *
	 * @param book
	 * @return books satisfying the search criteris entered by the patron
	 */
	List<Book> searchBookbyUser(Book book);

	/**
	 *
	 * @param id
	 * @return boolean status after deleting a book by librarian
	 */
	boolean deleteBookByID(Integer id);

	/**
	 *
	 * @param updatedbook
	 * @param request
	 * @return Book after updating book details
	 */

	Book updateBooks(Book updatedbook, HttpServletRequest request);

	/**
	 *
	 * @return the number of copies of a book available
	 */
	String getAvailableBookCount();

	/**
	 *
	 * @param isbn
	 * @return Book based on given isbn
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
	 * @return all Books
	 */

    List<Book> findAll();

	/**
	 * returns the checkout log for all users
	 * @return all user-book combinations
	 */

    List<LibUserBook> getAllLibUserBook();

	/**
	 *
	 * @param bookId
	 * @param userId
	 * @return status after renewing a book by a user userId
	 * @throws ParseException
	 */

	String renewBook(Integer bookId, Integer userId) throws ParseException;
}
