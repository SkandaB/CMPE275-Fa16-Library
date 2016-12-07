package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

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

    List<Book> findAll();

    Book getBookbyId(Integer bookId);

    String setBookRequest(Integer bookId, Integer userId) throws ParseException;

    /**
     * Search a book by any of its fields
     * @param book
     * @return A list of books that match the search criteria
     */
    List<Book> searchBook(Book book);

    /**
     * change status of book based on the number of copies checked out
     * @param book_Id
     */
    public void updateBookStatus(Integer book_Id) throws InterruptedException;

    /**
     * l
     * @param userId
     * @return list of books currently checked out by an user
     */

    public List<Book> getBookByUserId(Integer userId);

    /**
     *
     * @param bookId
     * @param userId
     *
     *
     */
    public String setBookReturn(Integer bookId, Integer userId);


    public List<LibUserBook> getAllLibUserBook();
    public String findCountAvailable();

    boolean deleteBookByID(Integer id);
}
