package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;

import javax.persistence.EntityManager;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBook;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


import javax.persistence.EntityExistsException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


@Transactional
@Repository
public class BookDaoImpl implements BookDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    /**
     * Add a book into database
     *
     * @param book
     */
    @Override
    public boolean addBook(Book book) {
        entityManager.persist(book);
        return true;
    }

    @Override

    public boolean addBook(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image) {
        Book book = new Book(isbn, author, title, callnumber, publisher, year_of_publication, location, num_of_copies, current_status, keywords, image);

        entityManager.persist(book);
        return true;
    }

    /**
     * Return the book by isbn code
     *
     * @param isbn
     * @return book object
     */
    @Override
    public Book getBookByISBN(String isbn) {
        return entityManager.find(Book.class, isbn);
    }

    
    @Override
    public List<Book> findAll(){
    	List<Book> books = (List<Book>) entityManager.createQuery("select b from Book b", Book.class).getResultList();
    	
        return books;
    	
    }

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.dao.BookDao#setBookRequest(edu.sjsu.cmpe275.lms.entity.User)
	 */
	@Override
	public String setBookRequest(Integer bookId,Integer userId) {
		// TODO Auto-generated method stub
		Book book = entityManager.find(Book.class, bookId);
		User user = entityManager.find(User.class, userId);
		
		UserBook userBook = new UserBook(book,user, LocalDate.now(),0);
		
		String returnStatus="";
		if(book.getCurrent_status().equalsIgnoreCase("available")){
			List<UserBook> currentUsers = book.getCurrentUsers();
			try{
				if(!currentUsers.contains(userBook)){
					currentUsers.add(userBook);
					book.setCurrentUsers(currentUsers);
					entityManager.persist(userBook);
					userBook.UserBookPersist(book, user);
					returnStatus = "User request for the book successful";
				}
				else{
					//have to include the loginc for extending the book 
					returnStatus = "User has checked out the same book already";
				}
				
			}catch(EntityExistsException e){
				returnStatus = "User has checked out the same book already";
			}
			
			
		}
		else{
			List<User> waitlist = book.getWaitlist();
			if(!waitlist.contains(user)){
				waitlist.add(user);
				book.setWaitlist(waitlist);
				returnStatus = "User is waitlisted! Waitlist number is "+book.getWaitlist().indexOf(user)+1;

			}
			else{
				returnStatus = "User has already requested for the book! Waitlist number is "+book.getWaitlist().indexOf(user)+1;
			}
					}
		entityManager.merge(book);
		
		return returnStatus;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.lms.dao.BookDao#getBookbyId(java.lang.Integer)
	 */
	@Override
	public Book getBookbyId(Integer bookId) {
		 
		Book book = entityManager.find(Book.class,bookId);
		return book;
	}

}
