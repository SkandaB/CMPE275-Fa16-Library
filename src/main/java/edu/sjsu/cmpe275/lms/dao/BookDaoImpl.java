package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.email.SendEmail;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.LibUserBook;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBook;
import edu.sjsu.cmpe275.lms.time.ClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableScheduling
@Transactional
@Repository
public class BookDaoImpl implements BookDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Autowired
    private SendEmail eMail;
    @Autowired
    private ClockService clockService;

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
     * @return List of all books
     */
    @Override
    public List<Book> findAll() {
        List<Book> books = entityManager.createQuery("select b from Book b", Book.class).getResultList();
        // System.out.println("Books " + books);
        return books;

    }

    /**
     *
     * @param bookId
     * @param userId
     * @return The status of book request by a user
     * @throws ParseException
     */

    @Override
    public String setBookRequest(Integer bookId, Integer userId) throws ParseException {
        // TODO Auto-generated method stub
        Book book = entityManager.find(Book.class, bookId);
        User user = entityManager.find(User.class, userId);


        String returnStatus = "";
        if (!book.getCurrent_status().equalsIgnoreCase("available") || !book.getWaitlist().isEmpty()) {
            if (book.getWtUId() == userId) {
                Calendar cal = clockService.getCalendar();

                UserBook userBook = new UserBook(book, user, cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), 0);

                String due_date = userBook.getDueDate();
                returnStatus = "Book Checkout Successful. \n The Due date is " + due_date + "\n";
                returnStatus = returnStatus + book.printBookInfo();

                book.setWtUId(-1);
                book.setLast_available_date(null);
                entityManager.merge(book);

                entityManager.persist(userBook);
                userBook.UserBookPersist(book, user);
                updateBookStatus(book.getBookId());
                System.out.println("after userbook persist " + book.getCurrentUsers().size() + "user " + user.getCurrentBooks().size());
                return returnStatus;


            }

            List<User> waitlist = book.getWaitlist();
            if (!waitlist.contains(user)) {
                waitlist.add(user);
                book.setWaitlist(waitlist);
                // entityManager.merge(book);
                /* do not change the return message. Further logic is dependent on the content of message*/
                returnStatus = "User is waitlisted!" + "\n" + "Waitlist number is " + (book.getWaitlist().indexOf(user) + 1) + "\n";
                returnStatus = returnStatus + book.toString();

            } else {
                /* do not change the return message. Further logic is dependent on the content of message*/
                returnStatus = "User has already requested for the book! Waitlist number is " + (book.getWaitlist().indexOf(user) + 1);
            }
            return returnStatus;

        } else {
            List<UserBook> currentUsers = book.getCurrentUsers();
            try {
                String userBookQuery = "select ub from UserBook ub where ub.book.bookId = " + bookId + " and ub.user.id = " + userId;
                UserBook ub = entityManager.createQuery(userBookQuery, UserBook.class).getSingleResult();
                if (ub != null) {
                    System.out.println("******* user has already checked out the same book *********");
                    return "User has already checked out the same book";
                }

            } catch (Exception e) {
                Calendar cal = clockService.getCalendar();

                UserBook userBook = new UserBook(book, user, cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), 0);

                String due_date = userBook.getDueDate();
                returnStatus = "Book Checkout Successful. \n The Due date is " + due_date + "\n";
                returnStatus = returnStatus + book.printBookInfo();

                entityManager.persist(userBook);
                userBook.UserBookPersist(book, user);
                updateBookStatus(book.getBookId());
                book.setWtUId(-1);
                return returnStatus;
            }
        }
        return "checkout not successfull";
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
     * @return Book given the bookId
     */
    @Override
    public Book getBookbyId(Integer bookId) {

        Book book = entityManager.find(Book.class, bookId);
        return book;
    }

    /**
     * Update the availability status of the book based on the checkout summary
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
     * @return the list of books checked out by a user
     */
    @Override
    public List<Book> getBookByUserId(Integer userId) {
        String userbookList = "select ub.book from UserBook ub where ub.user.id = " + userId;
        List<Book> books = entityManager.createQuery(userbookList, Book.class).getResultList();
        return books;
    }

    /**
     * @param bookId The ID of the book.
     * @param userId The ID of the user.
     * @return The return status of the book.
     */
    @Override
    public String setBookReturn(Integer bookId, Integer userId) {
        System.out.println("setBookReturn: starts now");
        String returnMessage = "";
        try {
            /*User user = entityManager.find(User.class, userId);*/
            Book book = entityManager.find(Book.class, bookId);
            String userbookQuery = "select ub from UserBook ub where ub.book.id = " + bookId + "and ub.user.id = " + userId;
            UserBook userBook = entityManager.createQuery(userbookQuery, UserBook.class).getSingleResult();

            if (!book.getWaitlist().isEmpty()) {
                waitlistMadeAvailable(userId, bookId);
                //User firstWaitlistUser = book.getWaitlist().get(0);
                //System.out.println("The first user in waitlist for this book is " + firstWaitlistUser.toString());
                //eMail.sendMail(firstWaitlistUser.getUseremail(), "Book Available", "The following book is available " + book.toString());

            } else {
                book.setCurrent_status("Available");

            }

            entityManager.refresh(userBook);
            System.out.println("setBookReturn: Book checkout date: " + userBook.getCheckout_date());

            returnMessage = "Book returned successfully: " + book.printBookInfo();
            userBook.setCalculateFine(clockService.getCalendar().getTime());
            if (userBook.getFine() > 0)
                returnMessage += "You did not return this book in time. Your fine is $" + userBook.getFine();

            entityManager.merge(book);

            entityManager.remove(userBook);

            return returnMessage;
        } catch (Exception e) {
            return "Some error occurred while returning book. Please contact system admin";
        }
    }

    /**
     * @return The count of the available books.
     */
    public String findCountAvailable() {
        Query query = entityManager.createQuery("select count(b.bookId) from Book b where b.current_status = :status");
        query.setParameter("status", "available");
        System.out.println("********** Available books= " + query.getResultList().get(0).toString());
        return query.getResultList().get(0).toString();
    }


    /**
     * All user - book checkout logs
     * @return
     */
    @Override
    public List<LibUserBook> getAllLibUserBook() {
        List<LibUserBook> libuserbooks = entityManager.createQuery("SELECT lub FROM LibUserBook lub", LibUserBook.class).getResultList();
        System.out.println("libuserbooks " + libuserbooks.get(0).toString());
        return libuserbooks;

    }

    /**
     * delete the book with given bookId
     * @param id
     * @return The return status of the books.
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

//    public String getBookISBN(String isbn){
//        Book bookentity = (Book) entityManager.createQuery("SELECT b FROM Book b where b.isbn = :value1")
//                .setParameter("value1", isbn).getSingleResult();
//        return bookentity.getIsbn();
//    }

    /**
     * Update book details
     *
     * @param book
     * @param request
     * @return updated book
     */
    public Book updateBooks(Book book, HttpServletRequest request) {
//        System.out.println("getBookISBN(book.getIsbn())++"+ getBookISBN(book.getIsbn()));
        System.out.println("Update book form +++ " + book);
        Book updatedbook = entityManager.find(Book.class, book.getBookId());
        updatedbook.setAuthor(book.getAuthor());
        updatedbook.setIsbn(book.getIsbn());
        updatedbook.setTitle(book.getTitle());
        updatedbook.setKeywords(book.getKeywords());
        updatedbook.setPublisher(book.getPublisher());
        updatedbook.setCallnumber(book.getCallnumber());
        updatedbook.setLocation(book.getLocation());
        if (book.getNum_of_copies() > updatedbook.getNum_of_copies()) {
            updatedbook.setNum_of_copies(book.getNum_of_copies());
            updatedbook.setCurrent_status("Available");
        } else {
            updatedbook.setNum_of_copies(book.getNum_of_copies());
        }
        updatedbook.setKeywords(book.getKeywords());
        // entityManager.merge(updatedbook);
//        Book bookEntity = entityManager.find(Book.class, book.getBookId());
        User user = (User) request.getSession().getAttribute("user");
        User userEntity = entityManager.find(User.class, user.getId());
        LibUserBook libUserBook = new LibUserBook(updatedbook, userEntity, "update");
        entityManager.merge(libUserBook);
        entityManager.flush();
        List<LibUserBook> addUpdateList = updatedbook.getListAddUpdateUsers();
        if (addUpdateList == null) {
            addUpdateList = new ArrayList<>();
        }
        addUpdateList.add(libUserBook);
        updatedbook.setListAddUpdateUsers(addUpdateList);
        entityManager.persist(updatedbook);
        entityManager.flush();
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

    /**
     * Renew the book
     * check if no user is in waitlist for the book
     * check if the user is not doing more that twice consecutively
     * @param bookId
     * @param userId
     * @return
     * @throws ParseException
     */

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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            userBook.setCheckout_date(dtf.format(clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            entityManager.merge(userBook);
            status = "Book renewed successfully. The new due date is " + userBook.getDueDate();
            eMail.sendMail(user.getUseremail(), "Book renew Successful", status);
            return status;
        }
        if (userBook.getRenew_flag() == 0) {
            userBook.setRenew_flag(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            userBook.setCheckout_date(dtf.format(clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            entityManager.merge(userBook);
            status = "Book renewed successfully. The new due date is " + userBook.getDueDate();
            eMail.sendMail(user.getUseremail(), "Book renew Successful", status);
            return status;

        }
        return status;

    }

    /**
     * Notify waitlisted user when the book becomes available
     * @param userId
     * @param bookId
     */
    @Override
    public void waitlistMadeAvailable(Integer userId, Integer bookId) {
        System.out.println("in waitlist available");
        Book book = entityManager.find(Book.class, bookId);
        List<User> waitlistedUser = book.getWaitlist();
        if (waitlistedUser.isEmpty()) {
            book.setCurrent_status("Available");
            book.setWtUId(-1);
            entityManager.merge(book);
        }
        //for (User user : waitlistedUser)
        else {
            User user = waitlistedUser.get(0);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            book.setLast_available_date(dtf.format(clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("The following book you requested for is now available. Please check out the book before " + bookAvailabilityDueDate(book) + "\n");
            emailBody.append("\n");
            emailBody.append(book.printBookInfo());
            waitlistedUser.remove(user);
            book.setWaitlist(waitlistedUser);
            book.setWtUId(user.getId());
            entityManager.merge(book);
            eMail.sendMail(user.getUseremail(), "Book is now available", emailBody.toString());
            System.out.println("mail sent to user " + user.getId());

        }
    }

    /**
     *
     * @param book
     * @return the date till when the book will be held for the waitlisted user
     */
    @Override
    public LocalDate bookAvailabilityDueDate(Book book) {


        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
        Date duedate = null;
        try {
            duedate = dtf.parse(book.getLast_available_date());
        } catch (ParseException e) {
            System.out.println("last available date exception");
            e.printStackTrace();
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(duedate);
        cal.add(Calendar.DATE, 3);


        //  String dueDate = dtf.format(cal.getTime());
        // System.out.println("String new due date " + dueDate);

        LocalDate date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        System.out.println(date);
        return date;
    }

    /**
     * Function to checkif watilisted user checkout the book
     * @param userId
     * @param bookId
     */
    public void didWLUserCheckoutBook(Integer userId, Integer bookId) {

        Book book = entityManager.find(Book.class, bookId);
        LocalDate timenow = clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (timenow.isAfter(bookAvailabilityDueDate(book))) {

            System.out.println("here inside if");

            try {
                UserBook userBook = entityManager.createQuery("select ub from UserBook ub where ub.book = " + bookId + " and ub.user = " + userId, UserBook.class).getSingleResult();

                DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
                Date chdate = dtf.parse(userBook.getCheckout_date());
                LocalDate checkoutDate = chdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Date availDate = dtf.parse(book.getLast_available_date());
                LocalDate lastAvailDate = availDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


                if (!checkoutDate.isAfter(lastAvailDate)) {
                    System.out.println("is not after");
                    waitlistMadeAvailable(userId, bookId);

                } else {
                    System.out.println("do nothing");
                }

            } catch (Exception e) {
                waitlistMadeAvailable(userId, bookId);
            }

        } else {
            //do nothing
        }
    }

    /**
     * Executes every two minutes
     * Waitlisted user loop execution
     * @throws ParseException
     */

    @Override
    @Scheduled(cron = "0 0/2  * * * ?")
    //@Scheduled(fixedDelay = 10000)
    public void waitlistCron() throws ParseException {
        System.out.println("in cron ");
        try {
            List<Book> books = findAll();
            if (books != null) {
                for (Book book : books) {
                    if (book.getWtUId() != -1) {
                        didWLUserCheckoutBook(book.getWtUId(), book.getBookId());
                    }

                }

            }

        } catch (Exception e) {

        }


    }


    /**
     * Executes every two minutes
     * Send email if the due date is approaching
     *
     * @throws ParseException
     */
    @Override
    @Scheduled(cron = "0 0/2 * * * ?")
    public void remainderEmailCron() throws ParseException {

        System.out.println("in remainder cron");

        List<UserBook> userBooks = entityManager.createQuery("select ub from UserBook ub", UserBook.class).getResultList();
        for (UserBook ub : userBooks) {

            DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date chdate = dtf.parse(ub.getDueDate());
            LocalDate checkoutDate = chdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            Date diff_in_date = Date.from(checkoutDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar cal = new GregorianCalendar();
            cal.setTime(diff_in_date);
            cal.add(Calendar.DATE, 5);
            LocalDate diffDate = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date diff_in_date1 = Date.from(checkoutDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            cal.setTime(diff_in_date1);
            cal.add(Calendar.DATE, -5);
            LocalDate start_diffdate = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            //Date data_now = clockService.getCalendar().getTime();   //Date.from(checkoutDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            //cal.setTime(data_now);
            //cal.add(Calendar.DATE, -5);
            LocalDate dateNow = clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            System.out.println("start " + start_diffdate + " now " + clockService.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() + " end " + checkoutDate);


            if (dateNow.isAfter(start_diffdate) && dateNow.isBefore(checkoutDate)   /*( checkoutDate.isEqual(diffDate) || checkoutDate.isBefore(diffDate))*/) {
                //have to send mail every data

                System.out.println("inside the if of date");

                StringBuilder emailBody = new StringBuilder();
                emailBody.append("The Due date for the following book is " + ub.getDueDate() + ". Please return the book to avoid fine \n");
                emailBody.append(ub.getBook().printBookInfo() + " \n");
                eMail.sendMail(ub.getUser().getUseremail(), "The due date is approaching", emailBody.toString());
            }

        }
    }

}
