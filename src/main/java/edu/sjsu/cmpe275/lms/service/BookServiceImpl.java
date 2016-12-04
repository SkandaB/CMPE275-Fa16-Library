/**
 * 
 */
package edu.sjsu.cmpe275.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;

/**
 * @author dhanyaramesh
 *
 */

@Service
public class BookServiceImpl implements BookService {

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.service.BookService#listBooks()
	 */
	
	@Autowired
	BookDao bookDao;
	
	@Override
	public List<Book> listBooks() {
		System.out.println("here");
		return bookDao.findAll();
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.service.BookService#findBook()
	 */
	@Override
	public Book findBook(String isbn) {
		// TODO Auto-generated method stub
		Book book = bookDao.getBookByISBN(isbn);
		return book;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.service.BookService#requestBook(java.lang.Integer)
	 */
	@Override
	public String requestBook(Integer bookId,Integer userId) {
		// TODO Auto-generated method stub
		return bookDao.setBookRequest(bookId,userId);
		
	}
	
	public Book findBookById(Integer bookId){
		Book book = bookDao.getBookbyId(bookId);
		return book;
	}
	
	

}
