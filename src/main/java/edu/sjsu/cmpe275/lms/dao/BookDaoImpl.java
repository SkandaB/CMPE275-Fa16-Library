package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.email.SendEmail;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
@Repository
public class BookDaoImpl implements BookDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Autowired
    private SendEmail eMail;

    /**
     * @param book
     * @return
     */
    @Override
    public boolean addBook(Book book) {
        entityManager.persist(book);
        return true;
    }

    /**
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
     * @param user
     * @return
     */
    @Override
    public boolean addBook(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image, User user) {
        Book book = new Book(isbn, author, title, callnumber, publisher, year_of_publication, location, num_of_copies, current_status, keywords, image);
        entityManager.persist(book);
        entityManager.flush();
        System.out.println("book" + book.getBookId());
        Book bookEntity = entityManager.find(Book.class, book.getBookId());
        User userEntity = entityManager.find(User.class, user.getId());
        LibUserBook libUserBook = new LibUserBook(bookEntity, userEntity, "add");
        entityManager.persist(libUserBook);
        List<LibUserBook> addUpdateList = bookEntity.getListAddUpdateUsers();
        if (addUpdateList == null) {
            addUpdateList = new ArrayList<>();
        }
        addUpdateList.add(libUserBook);
        bookEntity.setListAddUpdateUsers(addUpdateList);
        entityManager.merge(bookEntity);
        userEntity = entityManager.find(User.class, user.getId());
        List<LibUserBook> addUpdateList1 = userEntity.getAddUpdateList();
        if (addUpdateList1 == null) {
            addUpdateList1 = new ArrayList<>();
        }
        addUpdateList1.add(libUserBook);
        userEntity.setAddUpdateList(addUpdateList1);
        entityManager.merge(userEntity);
        entityManager.flush();
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

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        List<Book> books = (List<Book>) entityManager.createQuery("select b from Book b", Book.class).getResultList();
        System.out.println("Books " + books);
        return books;

    }

    /* (non-Javadoc)
     * @see edu.sjsu.cmpe275.lms.dao.BookDao#setBookRequest(edu.sjsu.cmpe275.lms.entity.User)
     */
    @Override
    public String setBookRequest(Integer bookId, Integer userId) throws ParseException {
        // TODO Auto-generated method stub
        Book book = entityManager.find(Book.class, bookId);
        User user = entityManager.find(User.class, userId);


        String returnStatus = "";
        if (!book.getCurrent_status().equalsIgnoreCase("available") || !book.getWaitlist().isEmpty()) {
            List<User> waitlist = book.getWaitlist();
            if (!waitlist.contains(user)) {
                waitlist.add(user);
                book.setWaitlist(waitlist);
                entityManager.merge(book);
                returnStatus = "User is waitlisted!" + "\n" + "Waitlist number is " + (book.getWaitlist().indexOf(user) + 1) + "\n";
                returnStatus = returnStatus + book.toString();

            } else {
                returnStatus = "User has already requested for the book! Waitlist number is " + (book.getWaitlist().indexOf(user) + 1);
            }
            return returnStatus;

        } else {
            List<UserBook> currentUsers = book.getCurrentUsers();
            try {
                String userBookQuery = "select ub from UserBook ub where ub.book.bookId = " + bookId + " and ub.user.id = " + userId;
                entityManager.createQuery(userBookQuery, UserBook.class).getSingleResult();
                return "User has already checked out the same book";
            } catch (Exception e) {

                UserBook userBook = new UserBook(book, user, LocalDate.now(), 0);

                String due_date = userBook.getDueDate();
                returnStatus = "User request for the book successful. \n The Due date is " + due_date + "\n";
                returnStatus = returnStatus + book.printBookInfo();

                entityManager.persist(userBook);
                userBook.UserBookPersist(book, user);
                updateBookStatus(book.getBookId());
                System.out.println("after userbook persist " + book.getCurrentUsers().size() + "user " + user.getCurrentBooks().size());
                return returnStatus;
            }
        }
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
                    q.setParameter((String) entry.getKey(), "%" + paramValues.get(entry.getKey()).toLowerCase() + "%");
                }
            }
        }

        List<Book> books = q.getResultList();
        return books;
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Book getBookbyId(Integer bookId) {

        Book book = entityManager.find(Book.class, bookId);
        return book;
    }

    /**
     * @param book_Id
     */
    @Override
    public void updateBookStatus(Integer book_Id) {

        String userbook_query = "select ub from UserBook ub where ub.book = " + book_Id;
        List<UserBook> userBooks = entityManager.createQuery(userbook_query, UserBook.class).getResultList();
        Book book = entityManager.find(Book.class, book_Id);
        if (book.getNum_of_copies() == userBooks.size()) {
            System.out.println("changing status");
            book.setCurrent_status("Hold");
            System.out.println("after changing in update fn " + book.getCurrent_status());
        }
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Book> getBookByUserId(Integer userId) {
        String userbookList = "select ub.book from UserBook ub where ub.user.id = " + userId;
        List<Book> books = entityManager.createQuery(userbookList, Book.class).getResultList();
        return books;

    }

    /**
     * @param bookId
     * @param userId
     * @return
     */
    @Override
    public String setBookReturn(Integer bookId, Integer userId) {
        try {
            /*User user = entityManager.find(User.class, userId);*/
            Book book = entityManager.find(Book.class, bookId);
            String userbookQuery = "select ub from UserBook ub where ub.book.id = " + bookId + "and ub.user.id = " + userId;
            UserBook userBook = entityManager.createQuery(userbookQuery, UserBook.class).getSingleResult();

            book.setCurrent_status("Available");
            if (!book.getWaitlist().isEmpty()) {
                User firstWaitlistUser = book.getWaitlist().get(0);
                System.out.println("The first user in waitlist for this book is " + firstWaitlistUser.toString());
                //eMail.sendMail(firstWaitlistUser.getUseremail(), "Book Available", "The following book is available " + book.toString());

            }

            entityManager.merge(book);

            entityManager.remove(userBook);

            return "Book returned successfully: " + book.printBookInfo();
        } catch (Exception e) {
            return "Some error occurred while returning book. Please contact system admin";
        }
    }

    /**
     * @return
     */
    public String findCountAvailable() {
        Query query = entityManager.createQuery("select count(b.bookId) from Book b where b.current_status = :status");
        query.setParameter("status", "available");
        System.out.println("********** Available books= " + query.getResultList().get(0).toString());
        return query.getResultList().get(0).toString();
    }

    /**
     * @return
     */
    @Override
    public List<LibUserBook> getAllLibUserBook() {
        List<LibUserBook> libuserbooks = entityManager.createQuery("SELECT lub FROM LibUserBook lub", LibUserBook.class).getResultList();
        System.out.println("libuserbooks " + libuserbooks.get(0).toString());
        return libuserbooks;

    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean deleteBookByID(Integer id) {
        Book book = entityManager.find(Book.class, id);
        List<User> waitlist = book.getWaitlist();
        if (!(waitlist.isEmpty())) {
            book.getWaitlist().removeAll(waitlist);
            System.out.println("book waitlist size after remove " + book.getWaitlist().size());
            entityManager.persist(book);
        }
        entityManager.remove(book);
        //remove_waitlist(book);
        return true;
    }

    public Book updateBooks(Book book, HttpServletRequest request) {
        entityManager.merge(book);
        Book bookEntity = entityManager.find(Book.class, book.getBookId());
        User user = (User) request.getSession().getAttribute("user");
        User userEntity = entityManager.find(User.class, user.getId());
        LibUserBook libUserBook = new LibUserBook(bookEntity, userEntity, "update");
        entityManager.persist(libUserBook);
        entityManager.flush();
        List<LibUserBook> addUpdateList = bookEntity.getListAddUpdateUsers();
        if (addUpdateList == null) {
            addUpdateList = new ArrayList<>();
        }
        addUpdateList.add(libUserBook);
        bookEntity.setListAddUpdateUsers(addUpdateList);
        entityManager.merge(bookEntity);
        userEntity = entityManager.find(User.class, user.getId());
        List<LibUserBook> addUpdateList1 = userEntity.getAddUpdateList();
        if (addUpdateList1 == null) {
            addUpdateList1 = new ArrayList<>();
        }
        addUpdateList1.add(libUserBook);
        userEntity.setAddUpdateList(addUpdateList1);
        entityManager.merge(userEntity);
        entityManager.flush();
        return book;
    }


    @Override
    public String setBookRenew(Integer bookId, Integer userId) throws ParseException {
        String status = "not renewed yet";
        UserBook userBook = entityManager.createQuery("select ub from UserBook ub where ub.book = " + bookId + " and ub.user = " + userId, UserBook.class).getSingleResult();
        Book book = entityManager.find(Book.class, bookId);
        User user = entityManager.find(User.class, userId);

        if (userBook.getFine() > 0) {
            Long userFine = entityManager.createQuery("select sum(ub.fine) from UserBook ub where ub.user = " + userId + " group by ub.user", Long.class).getSingleResult();
            status = "Cannot renew the book. You have an outstanding fine of $" + userFine;
            eMail.sendMail(user.getUseremail(), "Book renew Unsuccessfull", status);
            return status;
        }

        if (!book.getWaitlist().isEmpty()) {
            status = "Cannot renew book since there is a waitlist for this book. Please return the book by " + userBook.getDueDate() + " to avoid fine";
            eMail.sendMail(user.getUseremail(), "Book renew Unsuccessfull", status);
            return status;
        }


        if (userBook.getRenew_flag() == 2) {
            status = "Renewed the same book two times already. Cannot renew now. Please return the book by " + userBook.getDueDate() + " to avoid fine";
            eMail.sendMail(user.getUseremail(), "Book renew Unsuccessfull", status);
            return status;
        }
        if (userBook.getRenew_flag() == 1) {
            userBook.setRenew_flag(2);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            userBook.setCheckout_date(dtf.format(LocalDate.now()));
            entityManager.merge(userBook);
            status = "Book renewed successfully. The new due date is " + userBook.getDueDate();
            eMail.sendMail(user.getUseremail(), "Book renew Successfull", status);
            return status;
        }
        if (userBook.getRenew_flag() == 0) {
            userBook.setRenew_flag(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            userBook.setCheckout_date(dtf.format(LocalDate.now()));
            entityManager.merge(userBook);
            status = "Book renewed successfully. The new due date is " + userBook.getDueDate();
            eMail.sendMail(user.getUseremail(), "Book renew Successfull", status);
            return status;

        }
        return status;

    }

}
