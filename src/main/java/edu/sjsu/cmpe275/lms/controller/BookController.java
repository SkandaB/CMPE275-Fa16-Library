package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.errors.Errors;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
import com.google.gdata.data.books.VolumeEntry;
import com.google.gdata.data.books.VolumeFeed;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Publisher;
import com.google.gdata.util.ServiceException;
import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.errors.Errors;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;



@Controller
@RequestMapping("/book")
public class BookController {
    static final String API_KEY =
            "AIzaSyDnl1Qdtcfq2OtPSecoLIx7K5JtoM8u8z8";
    private static final String APPLICATION_NAME = "Library-System-Term-Project";
    private String isbn = "";

    @Autowired
    private BookDao bookDao;

    //comment
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAddBookPage() {
        ModelAndView modelAndView = new ModelAndView("addBook");
        return modelAndView;
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public ModelAndView getBook(@PathVariable("isbn") String isbn, HttpServletResponse response) {
        Book book = bookDao.getBookByISBN(isbn);
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
        //modelAndView.setViewName("addBook");
        return modelAndView;
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public
//    @ResponseBody
//    String uploadFileHandler(@ModelAttribute("book") Book book, @RequestParam("imagefile") MultipartFile imagefile, ModelAndView modelAndView) {
//        //@RequestParam("isbn") String isbn, @RequestParam("author") String author, @RequestParam("title") String title, @RequestParam("callnumber") String callnumber, @RequestParam("publisher") String publisher, @RequestParam("year_of_publication") int year_of_publication, @RequestParam("location") String location, @RequestParam("num_of_copies") String num_of_copies, @RequestParam("keywords") String keywords,
//      //  if (imagefile.isEmpty()) return "Failed to upload because the file was empty.";
//        try {
//            byte[] bytes = imagefile.getBytes();
//            //System.out.println(bytes);
//
//            book.setImage(bytes);
//            //bookDao.addBook(isbn, "xx", "xx", "xx", "xx", 2009, "xx", 5, "xx", "xx", bytes);
//            bookDao.addBook(book);
//
//            /*// Creating the directory to store file
//            String rootPath = System.getProperty("catalina.home");
//            File dir = new File(rootPath + File.separator + "tmpFiles");
//            if (!dir.exists()) dir.mkdirs();
//
//            // Create the file on server
//            File serverFile = new File(dir.getAbsolutePath()
//                    + File.separator + name);
//            BufferedOutputStream stream = new BufferedOutputStream(
//                    new FileOutputStream(serverFile));
//            stream.write(bytes);
//            stream.close();
//
//            System.out.println("Server File Location="
//                    + serverFile.getAbsolutePath());*/
//            modelAndView.addObject("book", new Book());
//            return "Successfully uploaded file";
//        } catch (Exception e) {
//            return "Failed to upload => " + e.getMessage();
//        }

//
//
//            throwNoISBNFoundError(response);
//            return null;

//        //byte[] encodeBase64 = Base64.encodeBase64(book.getImage());
//        String base64Encoded = "";
//        try {
//            base64Encoded = new String(encodeBase64, "UTF-8");
//        } catch (UnsupportedEncodingException uee) {
//            System.out.println("Error fetching image");
//        }

//        ModelAndView modelAndView = new ModelAndView("viewBook");
//        modelAndView.addObject("book", book);
//        //modelAndView.addObject("imageString", base64Encoded);
//        //modelAndView.setViewName("addBook");
//        return modelAndView;
//    }

//    @RequestMapping(method = RequestMethod.POST)
//    public
//    @ResponseBody
//    String uploadFileHandler(@ModelAttribute("book") Book book, @RequestParam(value = "imagefile", required = false) MultipartFile imagefile, ModelAndView modelAndView) {
//        if (imagefile.isEmpty()) return "Failed to upload because the file was empty.";
//        try {
//            byte[] bytes = imagefile.getBytes();
//            //System.out.println(bytes);
//
//            book.setImage(bytes);
//            //bookDao.addBook(isbn, "xx", "xx", "xx", "xx", 2009, "xx", 5, "xx", "xx", bytes);
//            bookDao.addBook(book);
//
//            /*// Creating the directory to store file
//            String rootPath = System.getProperty("catalina.home");
//            File dir = new File(rootPath + File.separator + "tmpFiles");
//            if (!dir.exists()) dir.mkdirs();
//            // Create the file on server
//            File serverFile = new File(dir.getAbsolutePath()
//                    + File.separator + name);
//            BufferedOutputStream stream = new BufferedOutputStream(
//                    new FileOutputStream(serverFile));
//            stream.write(bytes);
//            stream.close();
//            System.out.println("Server File Location="
//                    + serverFile.getAbsolutePath());*/
//            modelAndView.addObject("book", new Book());
//            return "Successfully uploaded file";
//        } catch (Exception e) {
//            return "Failed to upload => " + e.getMessage();
//        }
//    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/addBook", method = RequestMethod.POST)
    String addBookviaForm(@ModelAttribute("book") Book book, ModelAndView modelAndView, HttpServletResponse response) throws GeneralSecurityException, IOException, ServiceException {
        System.out.println("boook" + book);
        /**
         * Check if the mode of addition is via ISBN or advanced-mode.
         */
        if(book.getAuthor()== null){ // There was no author as input. Has to be simple mode.
            isbn = book.getIsbn();
            ISBNValidator validator = new ISBNValidator();
            if (validator.isValid(book.getIsbn())) {
                queryGoogleBooks(book, response);
            } else {
                throwNoISBNFoundError(response);
            }
        } else {
            //author was given as input. Has to be advanced mode.
            /**
             * Save value to database.
             */
            book.setIsbn(book.getIsbn());
            addNewBook(book,book.getTitle(),book.getAuthor(),book.getYear_of_publication(),book.getPublisher() ,response);
        }
        return "addBook";
    }

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

    public void queryGoogleBooks(Book book, HttpServletResponse response) throws GeneralSecurityException, IOException, ServiceException {
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
        addNewBook(book,title,author,year_of_publication,publisher,response);
    }

    private void addNewBook(Book book, String title, String author, String year_of_publication, String publisher , HttpServletResponse response) {
        try {
            bookDao.addBook(book.getIsbn(), author, title, book.getCallnumber(), publisher, year_of_publication, book.getLocation(), book.getNum_of_copies(), book.getCurrent_status(), book.getKeywords(),book.getImage());
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



}
