package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

/**
 * The BookDAO for the CRUD operations on Book Entity
 */
@EnableScheduling
@Transactional
public interface BookDao {

    /**
     * Add a book to database
     *
     * @param book
     * @return true if add successful, false if failed
     */
    boolean addBook(Book book);

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
     * Return the book by isbn code
     *
     * @param isbn
     * @return book object
     */
    Book getBookByISBN(String isbn);

    /**
     * Gets the list of all books.
     * @return List of all the books in the database.
     */
    List<Book> findAll();

    /**
     * Returns the book on the basis of book id.
     * @param bookId
     * @return book object.
     */
    Book getBookbyId(Integer bookId);

    /**
     *
     * @param bookId Id of the book to be requested.
     * @param userId Id of the user requesting the book.
     * @return the return status of the book.
     * @throws ParseException
     */
    String setBookRequest(Integer bookId, Integer userId) throws ParseException;

    /**
     * Search a book by any of its fields
     *
     * @param book object received from the search form.
     * @return A list of books that match the search criteria.
     */
    List<Book> searchBook(Book book);

    /**
     * change status of book based on the number of copies checked out
     *
     * @param book_Id Id of the book whose stats has to be updated.
     */
    void updateBookStatus(Integer book_Id) throws InterruptedException;

    /**
     * @param userId ID of the user
     * @return list of books currently checked out by an user
     */

    List<Book> getBookByUserId(Integer userId);

    /**
     * @param bookId ID of the book.
     * @param userId ID of the user.
     */
    String setBookReturn(Integer bookId, Integer userId);

    /**
     *
     * @return List of the all the book associated with a particular librarian.
     */
    List<LibUserBook> getAllLibUserBook();

    /**
     *
     * @return count of the total available books in the dashboard.
     */
    String findCountAvailable();

    /**
     *
     * @param id ID of the book to be deleted.
     * @return Status, if the book delete was successful or not.
     */
    boolean deleteBookByID(Integer id);

    /**
     *
     * @param updatedbook The book object as received from the form.
     * @param request The HTTP request parameter.
     * @return The newly updated book object.
     */
    Book updateBooks(Book updatedbook, HttpServletRequest request);

    /**
     * @param bookId The ID of the book.
     * @param userId The ID of the user.
     * @return the renew status
     */
    String setBookRenew(Integer bookId, Integer userId) throws ParseException;

    /**
     * returns the due date for check out for a waitlisted user when a book becomes available.
     * @param book The book object.
     * @return LocalDate object.
     */
    LocalDate bookAvailabilityDueDate(Book book);

    /**
     * @param bookId The Id of the book.
     * @param userId The Id of the user.
     */
    void waitlistMadeAvailable(Integer userId, Integer bookId);

    /**
     * Check if the waitlisted user checked out the book once it became available
     *
     * @param userId The ID os the user.
     * @param bookId The ID of the book.
     */
    void didWLUserCheckoutBook(Integer userId, Integer bookId);

    /**
     * cron to call didWLUserCheckoutBook
     */
    void waitlistCron() throws ParseException;

    /**
     * cron to send reminder emails starting 5 days prior to due date
     *
     * @throws ParseException
     */
    void remainderEmailCron() throws ParseException;
}
