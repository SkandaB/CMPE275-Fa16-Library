/**
 * 
 */
package edu.sjsu.cmpe275.lms.service;

import java.text.ParseException;
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
	public String requestBook(Integer bookId,Integer userId) throws ParseException {
		// TODO Auto-generated method stub
		return bookDao.setBookRequest(bookId,userId);
		
	}
	@Override
	public Book findBookById(Integer bookId){
		Book book = bookDao.getBookbyId(bookId);
		return book;
	}
	@Override
	public List<Book> listBooksOfUser(Integer userId){

		List<Book> books = bookDao.getBookByUserId(userId);


		return books;

	}

	@Override
	public String returnBook(Integer bookId,Integer userId){
		return bookDao.setBookReturn(bookId,userId);

	}

	@Override
	public List<Book> searchBookbyUser(Book book){

		return bookDao.searchBook(book);
	}
	

}
