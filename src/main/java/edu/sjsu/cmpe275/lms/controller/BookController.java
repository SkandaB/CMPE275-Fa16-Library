
package edu.sjsu.cmpe275.lms.controller;

import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
import com.google.gdata.data.books.VolumeEntry;
import com.google.gdata.data.books.VolumeFeed;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Publisher;
import com.google.gdata.util.ServiceException;
import edu.sjsu.cmpe275.lms.entity.*;
import edu.sjsu.cmpe275.lms.errors.Errors;
import edu.sjsu.cmpe275.lms.service.BookService;
import edu.sjsu.cmpe275.lms.time.ClockService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Book Controller for managing the books in LMS.
 */
@Component
@Controller
@EnableAspectJAutoProxy
@EnableScheduling
@RequestMapping("/book")
public class BookController {
    static final String API_KEY = "AIzaSyDnl1Qdtcfq2OtPSecoLIx7K5JtoM8u8z8";
    private static final String APPLICATION_NAME = "Library-System-Term-Project";
    @Autowired
    BookService bookService;

    @Autowired
    ClockService clockService;

    private String isbn = "";

    /**
     * direct to the add book page
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAddBookPage() {
        ModelAndView modelAndView = new ModelAndView("addBook");
        return modelAndView;
    }

    /**
     * @param isbn
     * @param response
     * @return
     */
    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public ModelAndView getBook(@PathVariable("isbn") String isbn, HttpServletResponse response) {
        Book book = bookService.getBookByISBN(isbn);
        /**
         * If ID is not found in database
         */
        if (book == null) {

            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(Errors.getISBNNotFoundErrorPage(isbn));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        byte[] encodeBase64 = Base64.encodeBase64(book.getImage());
        String base64Encoded = "";
        try {
            base64Encoded = new String(encodeBase64, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Error fetching image");
        }

        ModelAndView modelAndView = new ModelAndView("viewBook");
        modelAndView.addObject("book", book);
        modelAndView.addObject("imageString", base64Encoded);
        return modelAndView;
    }

    /**
     * @param book
     * @param modelAndView
     * @param request
     * @param response
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws ServiceException
     */
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/addBook", method = RequestMethod.POST)
    String addBookviaForm(@ModelAttribute("book") Book book, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException, IOException, ServiceException {
        System.out.println("boook" + book);
        /**
         * Check if the mode of addition is via ISBN or advanced-mode.
         */
        if (book.getAuthor() == null) { // There was no author as input. Has to be simple mode.
            isbn = book.getIsbn();
            ISBNValidator validator = new ISBNValidator();
            if (validator.isValid(book.getIsbn())) {
                User user = (User) request.getSession().getAttribute("user");
                System.out.println("user is - " + user.toString());
                queryGoogleBooks(book, response, user);
            } else {
                throwNoISBNFoundError(response);
            }
        } else {
            //author was given as input. Has to be advanced mode.
            /**
             * Save value to database.
             */
            User user = (User) request.getSession().getAttribute("user");
            System.out.println("user is - " + user.toString());
            book.setIsbn(book.getIsbn());
            addNewBook(book, book.getTitle(), book.getAuthor(), book.getYear_of_publication(), book.getPublisher(), response, user);
        }

        return "librarian/dashboard";
    }

    /**
     * @param response
     */
    private void throwNoISBNFoundError(HttpServletResponse response) {

        try {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(Errors.getISBNNotFoundErrorPage(isbn));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param book     Book object to be queried from google docs.
     * @param response The HTTP response object.
     * @param user     The user(librarian) who is processing the book.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws ServiceException
     */
    public void queryGoogleBooks(Book book, HttpServletResponse response, User user) throws GeneralSecurityException, IOException, ServiceException {
        BooksService booksService = new BooksService("Library-System-Term-Project");
        URL url = new URL("http://www.google.com/books/feeds/volumes/?q=ISBN%3C" + book.getIsbn() + "%3E");
        System.out.println("URL is " + url.toString());
        VolumeQuery volumeQuery = new VolumeQuery(url);
        VolumeFeed volumeFeed = booksService.query(volumeQuery, VolumeFeed.class);

        // using an ISBN in query gives only one entry in VolumeFeed
        List<VolumeEntry> volumeEntries = volumeFeed.getEntries();
        if (volumeEntries.size() == 0) {
            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(Errors.bookDetailsNotFound(isbn));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        VolumeEntry bookInfo = volumeEntries.get(0);
        /**
         * Extract Title
         */
        String title = bookInfo.getTitles().get(0).getValue().toString();
        System.out.println("Title: " + title);

        /**
         * Extract Author
         */
        int counter = 0;
        String author = "";
        List<Creator> authorList = bookInfo.getCreators();
        for (Creator indcreator : authorList) {
            counter += 1;
            if (counter < authorList.size()) author += indcreator.getValue() + ", ";
            else author += indcreator.getValue();
        }
        counter = 0;
        System.out.println("Author: " + author);

        /**
         * Extract Publisher list
         */
        String publisher = "";
        List<Publisher> publisherList = bookInfo.getPublishers();
        for (Publisher indpublisher : publisherList) {
            counter += 1;
            if (counter < publisherList.size()) publisher += indpublisher.getValue() + ", ";
            else publisher += indpublisher.getValue();
        }
        counter = 0;
        System.out.println("Publisher: " + publisher);

        /**
         * Year of Publication
         */
        String year_of_publication;

        year_of_publication = bookInfo.getDates().get(0).getValue();
        if (year_of_publication.contains("-")) {
            String[] yearOfPublication = year_of_publication.split("-");
            year_of_publication = yearOfPublication[0];
        }

        System.out.println("Year of Publication: " + year_of_publication);

        /**
         * Save the values to database
         */
        addNewBook(book, title, author, year_of_publication, publisher, response, user);
    }

    /**
     * @param book
     * @param title
     * @param author
     * @param year_of_publication
     * @param publisher
     * @param response
     * @param user
     */
    private void addNewBook(Book book, String title, String author, String year_of_publication, String publisher, HttpServletResponse response, User user) {
        try {
            bookService.addBook(book.getIsbn(), author, title, book.getCallnumber(), publisher, year_of_publication, book.getLocation(), book.getNum_of_copies(), book.getCurrent_status(), book.getKeywords(), book.getImage(), user);
        }
        /**
         * If Unique key number is tried to repeat
         */ catch (PersistenceException pe) {
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(Errors.getDuplicateKeyNotAllowedErrorPage(isbn));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * @param book1
     * @param modelAndView
     * @return The HTTP response-body to be sent to the calling javascript method.
     */
    @RequestMapping(value = "/searchAllBooks", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    List<BookPojo> searchAllBooks(@ModelAttribute("book") Book book1, ModelAndView modelAndView) {
        List<Book> books = bookService.findAll();
        List<BookPojo> booksList = new ArrayList<>();
        for (Book book : books) {
            BookPojo bookPojo = new BookPojo();
            bookPojo.setTitle(book.getTitle());
            bookPojo.setAuthor(book.getAuthor());
            bookPojo.setBookId(book.getBookId());
            bookPojo.setCallNumber(book.getCallnumber());
            bookPojo.setCurrentStatus(book.getCurrent_status());
            bookPojo.setNumberOfCopies(book.getNum_of_copies());
            bookPojo.setPublisher(book.getPublisher());
            bookPojo.setYearOfPublication(book.getYear_of_publication());
            bookPojo.setKeywords(book.getKeywords());
            bookPojo.setIsbn(book.getIsbn());
            bookPojo.setLocation(book.getLocation());
            booksList.add(bookPojo);
        }
        return booksList;
    }

    /**
     * @param isJson   is the request in json format
     * @param response The JSON response body
     * @return
     */
    @Transactional
    @RequestMapping(method = RequestMethod.GET, params = "json", produces = "application/json; charset=UTF-8")
    public
    @ResponseBody
    String getBooksJson(@RequestParam(value = "json") String isJson, HttpServletResponse response) {
        if (isJson.equals("true")) {
            List<Book> booklist = bookService.findAll();
            /**
             * If ID is not found in database
             */
            if (booklist.size() < 1) {
                try {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }
        return "{\"Error\":\"json=" + isJson + " not a valid value\"}";
    }

    /**
     * @param book The book object to be searched.
     * @param id   The ID of the book to be searched.
     * @return
     */
    @RequestMapping(value = "/books/{book_id}", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    Book searchBookByID(@ModelAttribute("book") Book book, @PathVariable("book_id") Integer id) {
        Book res_book = bookService.findBookById(id);
        return res_book;
    }

    /**
     * @param libUserBookPojo
     * @param modelAndView
     * @return ResponseBody The response body of the the javascript calling object.
     */
    @RequestMapping(value = "/getAllLibUserBook", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    HashMap<Integer, List<LibUserBookPojo>> searchAllUserLibBooks(@ModelAttribute("libUserBookPojo") LibUserBookPojo libUserBookPojo, ModelAndView modelAndView) {
        System.out.println("Inside searchAllUserLibBooks !!!");
        List<LibUserBook> libUserBooks = bookService.getAllLibUserBook();
        System.out.println("libUserBooks " + libUserBooks.get(0).toString());
        HashMap<Integer, List<LibUserBookPojo>> hm = new HashMap<Integer, List<LibUserBookPojo>>();
        for (LibUserBook libUserBook : libUserBooks) {
            LibUserBookPojo libUserBookPojo1 = new LibUserBookPojo();
            libUserBookPojo1.setAction(libUserBook.getAction());
            libUserBookPojo1.setAuthor(libUserBook.getBook().getAuthor());
            libUserBookPojo1.setBookId(libUserBook.getBook().getBookId());
            libUserBookPojo1.setBookName(libUserBook.getBook().getTitle());
            libUserBookPojo1.setIsbn(libUserBook.getBook().getIsbn());
            libUserBookPojo1.setNoOfCopies(libUserBook.getBook().getNum_of_copies());
            libUserBookPojo1.setStatus(libUserBook.getBook().getCurrent_status());
            libUserBookPojo1.setTitle(libUserBook.getBook().getTitle());
            libUserBookPojo1.setUserEmail(libUserBook.getUser().getUseremail());
            libUserBookPojo1.setUserId(libUserBook.getUser().getId());
            libUserBookPojo1.setUserName(libUserBook.getUser().getUseremail());
            if (hm.containsKey(libUserBookPojo1.getUserId())) {
                hm.get(libUserBookPojo1.getUserId()).add(libUserBookPojo1);
            } else {
                List<LibUserBookPojo> lubp = new ArrayList<>();
                lubp.add(libUserBookPojo1);
                hm.put(libUserBookPojo1.getUserId(), lubp);
            }
        }
        return hm;
    }

    /**
     * @param book         The book object to be updated.
     * @param modelAndView
     * @param request      The model and view
     * @return
     */
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/updatebook", method = RequestMethod.POST)
    public ModelAndView updateBooks(@ModelAttribute("book") Book book, ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView = new ModelAndView("librarian/dashboard");
        bookService.updateBooks(book, request);
        System.out.println("Update called !!!");
        return modelAndView;
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletebook/{book_id}", method = RequestMethod.GET)
    public ModelAndView deleteBook(@PathVariable("book_id") Integer id) {
        try {
            System.out.println("User requested to delete this book: " + id);
            if (bookService.deleteBookByID(id)) {
                System.out.println("Book Deleted Sucessfully!!");
                return new ModelAndView(new RedirectView("/dashboard", true));
            } else {
                return new ModelAndView("error").addObject("errorMessage", "Unable to delete book, book is checked out already.");
            }
        } catch (Exception e) {
            return new ModelAndView("error").addObject("errorMessage", "Unable to delete book at this time. Please try later");
        }
    }
}

