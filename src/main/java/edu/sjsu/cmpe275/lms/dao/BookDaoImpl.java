package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.Book;

import javax.persistence.*;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBook;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public List<Book> findAll() {
        List<Book> books = entityManager.createQuery("select b from Book b", Book.class).getResultList();
        return books;

    }

    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.dao.BookDao#setBookRequest(edu.sjsu.cmpe275.lms.entity.User)
     */
    @Override
    public String setBookRequest(Integer bookId, Integer userId) {
        // TODO Auto-generated method stub
        Book book = entityManager.find(Book.class, bookId);
        User user = entityManager.find(User.class, userId);

        UserBook userBook = new UserBook(book, user, LocalDate.now(), 0);

        String returnStatus = "";
        if (book.getCurrent_status().equalsIgnoreCase("available")) {
            List<UserBook> currentUsers = book.getCurrentUsers();
            try {
                if (!currentUsers.contains(userBook)) {
                    currentUsers.add(userBook);
                    book.setCurrentUsers(currentUsers);
                    entityManager.persist(userBook);
                    userBook.UserBookPersist(book, user);
                    returnStatus = "User request for the book successful";
                } else {
                    //have to include the loginc for extending the book
                    returnStatus = "User has checked out the same book already";
                }

            } catch (EntityExistsException e) {
                returnStatus = "User has checked out the same book already";
            }


        } else {
            List<User> waitlist = book.getWaitlist();
            if (!waitlist.contains(user)) {
                waitlist.add(user);
                book.setWaitlist(waitlist);
                returnStatus = "User is waitlisted! Waitlist number is " + book.getWaitlist().indexOf(user) + 1;

            } else {
                returnStatus = "User has already requested for the book! Waitlist number is " + book.getWaitlist().indexOf(user) + 1;
            }
        }
        entityManager.merge(book);

        return returnStatus;
    }

    /**
     * Search a book by any of its fields
     *
     * @param book
     * @return A list of books that match the search criteria
     */
    @Override
    public List<Book> searchBook(Book book) {
        StringBuilder querySB = new StringBuilder("Select b From Book b WHERE");
        Query q;

        //if isbn is present, all other fields are ignored
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            querySB.append(" b.isbn = :isbn");
            q = entityManager.createQuery(querySB.toString(), Book.class);
            q.setParameter("isbn", book.getIsbn().toLowerCase());
            //List<Book> books = q.getResultList();
            //return books;
        } else {

            // if this is the first parameter in query
            boolean first = true;

            // map will hold what all parameters are present
            HashMap<String, Boolean> paramPresent = new HashMap<>();
            paramPresent.put("author", false);
            paramPresent.put("title", false);
            paramPresent.put("callnumber", false);
            paramPresent.put("publisher", false);
            paramPresent.put("year_of_publication", false);
            paramPresent.put("current_status", false);

            // map will hold what values of all parameters
            HashMap<String, String> paramValues = new HashMap<>();
            paramValues.put("author", book.getAuthor());
            paramValues.put("title", book.getTitle());
            paramValues.put("callnumber", book.getCallnumber());
            paramValues.put("publisher", book.getPublisher());
            paramValues.put("year_of_publication", book.getYear_of_publication());
            paramValues.put("current_status", book.getCurrent_status());

            if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.author) LIKE :author");
                paramPresent.put("author", true);
            }

            if (book.getTitle() != null && !book.getTitle().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.title) LIKE :title");
                paramPresent.put("title", true);
            }

            if (book.getCallnumber() != null && !book.getCallnumber().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.callnumber) LIKE :callnumber");
                paramPresent.put("callnumber", true);
            }

            if (book.getPublisher() != null && !book.getPublisher().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.publisher) LIKE :publisher");
                paramPresent.put("publisher", true);
            }

            if (book.getLocation() != null && !book.getLocation().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.year_of_publication) LIKE :year_of_publication");
                paramPresent.put("year_of_publication", true);
            }

            if (book.getCurrent_status() != null && !book.getCurrent_status().isEmpty()) {
                // append 'and' if first parameter
                if (!first) {
                    querySB.append(" AND");
                }
                first = false;
                querySB.append(" lower(b.current_status) LIKE :current_status");
                paramPresent.put("current_status", true);
            }

            q = entityManager.createQuery(querySB.toString(), Book.class);

            for (Map.Entry entry : paramPresent.entrySet()) {
                if ((Boolean) entry.getValue()) {
                    // set query parameters for those which have a value
                    q.setParameter((String) entry.getKey(), "%"+paramValues.get(entry.getKey()).toLowerCase()+"%");
                }
            }
        }

        List<Book> books = q.getResultList();
        return books;
    }

    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.dao.BookDao#getBookbyId(java.lang.Integer)
     */
    @Override
    public Book getBookbyId(Integer bookId) {

        Book book = entityManager.find(Book.class, bookId);
        return book;
    }

	@Override
	public void updateBookStatus(Integer book_Id){
		String book_query = "select b from Book b where b.bookId = " + book_Id;

 		Book book = (Book) entityManager.createQuery(book_query, Book.class).getSingleResult();

		System.out.println("book "+book.getBookId());

		String userbook_query = "select ub from UserBook ub where ub.book.bookId = "+book_Id;


		List<UserBook> userBooks = 	entityManager.createQuery(userbook_query, UserBook.class).getResultList();

		System.out.println("userbook "+userBooks.size());

		if(book.getNum_of_copies()==userBooks.size()){
			System.out.println("changing status");
			book.setCurrent_status("Hold");
			entityManager.merge(book);
		}
	}

}
