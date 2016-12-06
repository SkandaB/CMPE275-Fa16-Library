/**
 * 
 */
package edu.sjsu.cmpe275.lms.service;

import java.text.ParseException;
import java.util.List;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;

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
	public void returnBook(Integer bookId,Integer userId);

}
