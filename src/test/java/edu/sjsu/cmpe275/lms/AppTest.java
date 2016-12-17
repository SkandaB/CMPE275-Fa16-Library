package edu.sjsu.cmpe275.lms;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@SuppressWarnings("deprecation")
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" })
@WebAppConfiguration
public class AppTest {
    @Autowired//(required = true)
    private BookDao bookDao;

    private Book book1;
    private Book book2;

    @Before
    public void setUp() {
        book1 = new Book("1111111111111", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 5, "Available", "test,book",null);
        book2 = new Book("2222222222222", "Author", "Title", "12:34:56", "Publisher", "2016", "MLK", 5, "Available", "test,book",null);
    }

    @Test
    public void addBookTest() {
        bookDao.addBook(book1);
        int id = book1.getBookId();
        Assert.assertEquals(bookDao.getBookbyId(id), book1);
        //cleanup
        bookDao.deleteBookByID(id);
    }

    @Test
    public void findAllTest() {
        bookDao.addBook(book1);
        bookDao.addBook(book2);
        List<Book> books = bookDao.findAll();
        Assert.assertEquals(books.size(), 2);
        Assert.assertEquals(books.get(0), book1);
        Assert.assertEquals(books.get(1), book2);
        //cleanup
        bookDao.deleteBookByID(books.get(0).getBookId());
        bookDao.deleteBookByID(books.get(1).getBookId());
    }
}
