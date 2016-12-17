package edu.sjsu.cmpe275.lms;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.dao.UserBookDao;
import edu.sjsu.cmpe275.lms.dao.UserDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
//@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" })
@WebAppConfiguration
public class AppTest {
    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBookDao userBookDao;

    private Book book1;
    private Book book2;
    private Book book3;

    private User user1;

    private UserBook userBook1;

    /**
     * Set-up and initializations
     */
    @Before
    public void setUp() {
        book1 = new Book("1111111111111", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 5, "Available", "test,book",null);
        book2 = new Book("2222222222222", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 5, "Available", "test,book",null);
        book3 = new Book("3333333333333", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 1, "Available", "test,book",null);
        user1 = new User(123456, "user@gmail.com","password","ROLE_USER",true);

        userBook1 = new UserBook(book1, user1, LocalDateTime.now(), 0);
    }

    /**
     * BOOK tests
     */

    @Test
    public void deleteBookByIDTest() {
        bookDao.addBook(book1);
        int id = book1.getBookId();
        Assert.assertNotNull(bookDao.getBookbyId(id));
        bookDao.deleteBookByID(id);
        Assert.assertNull(bookDao.getBookbyId(id));
    }

    @Test
    public void getBookbyIdTest() {
        bookDao.addBook(book1);
        int id = book1.getBookId();
        Assert.assertEquals(book1, bookDao.getBookbyId(id));
        //cleanup
        bookDao.deleteBookByID(id);
    }

    @Test
    public void addBookTest() {
        bookDao.addBook(book1);
        int id = book1.getBookId();
        Assert.assertEquals(book1, bookDao.getBookbyId(id));
        //cleanup
        bookDao.deleteBookByID(id);
    }

    @Test
    public void findAllTest() {
        bookDao.addBook(book1);
        bookDao.addBook(book2);
        List<Book> books = bookDao.findAll();
        Assert.assertEquals(books.size(), 2);
        Assert.assertEquals(book1, books.get(0));
        Assert.assertEquals(book2, books.get(1));
        //cleanup
        bookDao.deleteBookByID(books.get(0).getBookId());
        bookDao.deleteBookByID(books.get(1).getBookId());
    }

    @Test
    public void updateBookStatusTest() {
        userDao.createUser(user1);
        int userid = user1.getId();

        bookDao.addBook(book1);
        int bookid1 = book1.getBookId();
        try {
            bookDao.setBookRequest(bookid1, userid);
        } catch (ParseException pe) {}
        try {
            bookDao.updateBookStatus(bookid1);
        } catch (InterruptedException ie) {}

        Assert.assertEquals("Available", book1.getCurrent_status());

        bookDao.addBook(book3);
        int bookid3 = book3.getBookId();
        try {
            bookDao.setBookRequest(bookid3, userid);
        } catch (ParseException pe) {}
        try {
            bookDao.updateBookStatus(bookid3);
        } catch (InterruptedException ie) {}

        Assert.assertEquals("Hold",book3.getCurrent_status());

        //cleanup
        book3 = null;
        book3 = new Book("3333333333333", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 1, "Available", "test,book",null);
        bookDao.deleteBookByID(bookid1);
        bookDao.deleteBookByID(bookid3);
        userDao.removeUser(userid);
        userBookDao.removeUserBook(userBook1);

    }

    /**
     * USER Tests
     */

    @Test
    public void removeUserTest() {
        userDao.createUser(user1);
        int id = user1.getId();
        Assert.assertNotNull(userDao.getUser(id));
        userDao.removeUser(id);
        Assert.assertNull(userDao.getUser(id));
    }

    @Test
    public void createUserTest() {
        userDao.createUser(user1);
        int id = user1.getId();
        Assert.assertEquals(user1, userDao.getUser(id));
        //cleanup
        userDao.removeUser(id);
    }


}
