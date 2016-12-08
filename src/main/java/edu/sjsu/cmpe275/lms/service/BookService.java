/**
 * 
 */
package edu.sjsu.cmpe275.lms.service;

import edu.sjsu.cmpe275.lms.entity.Book;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @author dhanyaramesh
 *
 */
public interface BookService {
	
	public List<Book> listBooks();
	public Book findBook(String isbn);
	public String requestBook(Integer bookId,Integer userId) throws ParseException;
	public Book findBookById(Integer bookId);
	public List<Book> listBooksOfUser(Integer userId);
	public String returnBook(Integer bookId,Integer userId);
	public List<Book> searchBookbyUser(Book book);

	public boolean deleteBookByID(Integer id);
	public Book updateBooks(Book updatedbook ,HttpServletRequest request);
	String getAvailableBookCount();
}
