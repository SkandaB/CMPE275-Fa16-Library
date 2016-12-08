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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


    @Autowired
    private SendEmail eMail;

    @Override
    public boolean addBook(Book book) {
        entityManager.persist(book);
        return true;
    }

    @Override

    public boolean addBook(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image, User user) {
        Book book = new Book(isbn, author, title, callnumber, publisher, year_of_publication, location, num_of_copies, current_status, keywords, image);
/*        List<User> addUpdateList = book.getAddUpdateUserlist();
        User userEntity = entityManager.find(User.class,user.getId());
        if(addUpdateList==null){
            addUpdateList = new ArrayList<>();
        }
        addUpdateList.add(userEntity);
        book.setAddUpdateUserlist(addUpdateList);
        entityManager.persist(book);
        entityManager.flush();
        LibUserBook libUserBook = new LibUserBook(book,user,"add");
        List<LibUserBook> libUpdateList = userEntity.getAddUpdateList();
        if(libUpdateList==null){
            libUpdateList = new ArrayList<LibUserBook>();
        }
        libUpdateList.add(libUserBook);
        userEntity.setAddUpdateList(libUpdateList);
        entityManager.merge(userEntity);*/
        entityManager.persist(book);
        entityManager.flush();
        System.out.println("book" + book.getBookId());
        Book bookEntity = entityManager.find(Book.class, book.getBookId());
        User userEntity = entityManager.find(User.class, user.getId());
        LibUserBook libUserBook = new LibUserBook(bookEntity, userEntity, "add");
        entityManager.persist(libUserBook);
        // newly add code
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


    @Override
    public List<Book> findAll() {
        List<Book> books = (List<Book>) entityManager.createQuery("select b from Book b", Book.class).getResultList();
        System.out.println("Books "+books);
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
        if (!book.getCurrent_status().equalsIgnoreCase("available")) {
            List<User> waitlist = book.getWaitlist();
            if (!waitlist.contains(user)) {
                waitlist.add(user);
                book.setWaitlist(waitlist);
                entityManager.merge(book);
                returnStatus = "User is waitlisted! Waitlist number is " + (book.getWaitlist().indexOf(user) + 1) + "\n";
                returnStatus = returnStatus+book.toString();
                eMail.sendMail(user.getUseremail(), returnStatus, returnStatus);

            } else {
                returnStatus = "User has already requested for the book! Waitlist number is " + (book.getWaitlist().indexOf(user) + 1);
            }
            return returnStatus;

        }
        else {
            List<UserBook> currentUsers = book.getCurrentUsers();
            try{
                String userBookQuery = "select ub from UserBook ub where ub.book.bookId = "+bookId + " and ub.user.id = "+userId;
                UserBook thisub = entityManager.createQuery(userBookQuery,UserBook.class).getSingleResult();

                return "User has already checked out the same book";




            }catch (Exception e){


                UserBook userBook = new UserBook(book, user, LocalDate.now(), 0);
                //currentUsers.add(userBook);
                // book.setCurrentUsers(currentUsers);

                String due_date = userBook.getDueDate();
                returnStatus = "User request for the book successful. \n The Due date is " + due_date + "\n";
                returnStatus = returnStatus+book.toString();

                entityManager.persist(userBook);
                updateBookStatus(book.getBookId());

                eMail.sendMail(user.getUseremail(), returnStatus, returnStatus);
                //entityManager.persist(book);

                //userBook.UserBookPersist(book, user);
                System.out.println("after mail book status "+book.getCurrent_status());


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

        System.out.println("in update");
        String userbook_query = "select ub from UserBook ub where ub.book = " + book_Id;


        List<UserBook> userBooks = entityManager.createQuery(userbook_query, UserBook.class).getResultList();

        System.out.println("userbook " + userBooks.size());
        // if(userBooks.size()>=0){

        Book book = entityManager.find(Book.class, book_Id);
        if (book.getNum_of_copies() == userBooks.size()) {
            System.out.println("changing status");
            book.setCurrent_status("Hold");
            //  entityManager.persist(book);

            System.out.println("after changing in update fn " + book.getCurrent_status());
        }

        // }


    }


    @Override
    public List<Book> getBookByUserId(Integer userId){
        String userbookList = "select ub.book from UserBook ub where ub.user.id = "+userId;
        List<Book> books= entityManager.createQuery(userbookList,Book.class).getResultList();
        System.out.println("user book list based on user id "+books.size());
        return books;

    }

    @Override
    public String setBookReturn(Integer bookId, Integer userId){

        try{
            String userbookQuery = "select ub from UserBook ub where ub.book.id = " + bookId + "and ub.user.id = "+userId;
            UserBook userBook = entityManager.createQuery(userbookQuery,UserBook.class).getSingleResult();
            entityManager.remove(userBook);
            return "Book returned successfully";
        }catch(Exception e){

            return "Invalid Book";
        }



       // entityManager.persist(userBook);
        // entityManager.persist(userBook);

//        Book book = entityManager.find(Book.class,bookId);
//        List<UserBook> temp = book.getCurrentUsers();
//        temp.remove(userBook);
//        book.setCurrentUsers(temp);
//        entityManager.persist(book);


    }

    public String findCountAvailable() {
        Query query = entityManager.createQuery("select count(b.bookId) from Book b where b.current_status = :status");
        query.setParameter("status", "available");
        System.out.println("********** Available books= " + query.getResultList().get(0).toString());
        return query.getResultList().get(0).toString();
        /*List<Integer> bookIds = query.getResultList();
        System.out.println("Counts from DB "+bookIds.get(0));
        if (bookIds.size() > 0) {
            return bookIds.size();
        }
        return 0;*/
    }

    @Override
    public List<LibUserBook> getAllLibUserBook() {
        List<LibUserBook> libuserbooks = (List<LibUserBook>) entityManager.createQuery("SELECT lub FROM LibUserBook lub", LibUserBook.class).getResultList();
        System.out.println("libuserbooks " + libuserbooks.get(0).toString());
        return libuserbooks;

    }

    @Override
    public boolean deleteBookByID(Integer id) {
        Book book = entityManager.find(Book.class, id);
        List<User> waitlist = book.getWaitlist();
        if (!(waitlist.isEmpty())) {
            waitlist.clear();
            entityManager.merge(book);
        }
        entityManager.remove(book);
        //remove_waitlist(book);
        return true;
    }

/*
    public boolean remove_waitlist(Book book) {

        List<User> waitlist = book.getWaitlist();
        waitlist.clear();
        book.setWaitlist(waitlist);
        entityManager.persist(book);
        return true;
    }*/

    public Book updateBooks(Book book, HttpServletRequest request){
            entityManager.merge(book);
            //entityManager.flush();
//        System.out.println("book" + bookupdated.getBookId());
//        entityManager.merge(book);
//        entityManager.flush();
//        Book bookEntity = entityManager.find(Book.class, book.getBookId());
//        User user = (User) request.getSession().getAttribute("user");
//        User userEntity = entityManager.find(User.class, user.getId());
//        LibUserBook libUserBook = new LibUserBook(bookEntity, userEntity, "update");
//        entityManager.merge(libUserBook);
//        entityManager.flush();
//        // newly add code
//        List<LibUserBook> addUpdateList = bookEntity.getListAddUpdateUsers();
//        if (addUpdateList == null) {
//            addUpdateList = new ArrayList<>();
//        }
//        addUpdateList.add(libUserBook);
//        bookEntity.setListAddUpdateUsers(addUpdateList);
//        entityManager.merge(bookEntity);
//        userEntity = entityManager.find(User.class, user.getId());
//        List<LibUserBook> addUpdateList1 = userEntity.getAddUpdateList();
//        if (addUpdateList1 == null) {
//            addUpdateList1 = new ArrayList<>();
//        }
//        addUpdateList1.add(libUserBook);
//        userEntity.setAddUpdateList(addUpdateList1);
//        entityManager.merge(userEntity);
//        entityManager.flush();
        return book;
    }

}
