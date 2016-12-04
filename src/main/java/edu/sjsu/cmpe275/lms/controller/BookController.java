package edu.sjsu.cmpe275.lms.controller;

/**
 * Created by Sagar on 12/2/2016.
 */
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
import com.google.gdata.data.AbstractExtension;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.google.gdata.data.Person;
import com.google.gdata.data.books.VolumeEntry;
import com.google.gdata.data.books.VolumeFeed;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.dublincore.Publisher;
import com.google.gdata.util.ServiceException;
import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.errors.Errors;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
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
            throwNoISBNFoundError(response);
            return null;
        }
//        //byte[] encodeBase64 = Base64.encodeBase64(book.getImage());
//        String base64Encoded = "";
//        try {
//            base64Encoded = new String(encodeBase64, "UTF-8");
//        } catch (UnsupportedEncodingException uee) {
//            System.out.println("Error fetching image");
//        }

        ModelAndView modelAndView = new ModelAndView("viewBook");
        modelAndView.addObject("book", book);
        //modelAndView.addObject("imageString", base64Encoded);
        //modelAndView.setViewName("addBook");
        return modelAndView;
    }

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


    @RequestMapping(value = "/addBook/", method = RequestMethod.POST)
    String addBookviaISBN(@ModelAttribute("book") Book book, ModelAndView modelAndView, HttpServletResponse response) throws GeneralSecurityException, IOException, ServiceException {
        System.out.println("boook"+book);
        isbn = book.getIsbn();

        ISBNValidator validator = new ISBNValidator();
        if(validator.isValid(book.getIsbn())) {
            queryGoogleBooks(isbn);
        } else {
            throwNoISBNFoundError(response);
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

        public void queryGoogleBooks(String isbn) throws GeneralSecurityException, IOException, ServiceException {
        errorIfNotSpecified();
        BooksService booksService = new BooksService("Library-System-Term-Project");
        URL url = new URL("http://www.google.com/books/feeds/volumes/?q=ISBN%3C" + isbn + "%3E");
        System.out.println("URL is "+url.toString());
        VolumeQuery volumeQuery = new VolumeQuery(url);
        VolumeFeed volumeFeed = booksService.query(volumeQuery, VolumeFeed.class);

        // using an ISBN in query gives only one entry in VolumeFeed
        List<VolumeEntry> volumeEntries = volumeFeed.getEntries();
        VolumeEntry bookInfo = volumeEntries.get(0);


        /**
         * Extract Title
         */
        String title = bookInfo.getTitles().get(0).getValue().toString();
        System.out.println("Title: " + title);

        /**
         * Extract Author
         */
        int counter =0;
        String author = "";
        List<Creator> authorList = bookInfo.getCreators();
        for(Creator indcreator: authorList){
            counter +=1;
            if(counter<authorList.size())  author += indcreator.getValue()+", ";
            else author += indcreator.getValue();
        }
        counter =0;
        System.out.println("Author: "+author);

        /**
         * Extract Publisher list
         */
        String publisher = "";
        List<Publisher> publisherList = bookInfo.getPublishers();
        for(Publisher indpublisher: publisherList){
            counter +=1;
            if(counter<publisherList.size()) publisher += indpublisher.getValue()+", ";
            else publisher += indpublisher.getValue();
        }
        counter = 0;
        System.out.println("Publisher: "+publisher);

        /**
         * Year of Publication
         */
        int year_of_publication= 0;
        List<Date> datesList = bookInfo.getDates();
        year_of_publication = Integer.parseInt(datesList.get(0).getValue());
        System.out.println("Year of Publication: "+year_of_publication);

        /**
         * HardCoded values
         */
        String callnumber = "1254345";
        String location = "floor-7";
        int num_of_copies = 4;
        String current_status= "available";
        String keywords ="";

        /**
         * Save the values to database
         */
        bookDao.addBook(isbn,author,title,callnumber,publisher,year_of_publication,location,num_of_copies,current_status,keywords);
        // Set up Books client.
//        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
//                .setApplicationName(APPLICATION_NAME)
//                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
//                .build();
//        // Set query string and filter only Google eBooks.
//        System.out.println("Query: [" + query + "]");
//        Books.Volumes.List volumesList = books.volumes().list(query);
//        //volumesList.setFilter("ebooks");
//
//        // Execute the query.
//        Volumes volumes = volumesList.execute();
//        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
//            System.out.println("No matches found.");
//            return;
//        }
//
//        // Output results.
//        for (Volume volume : volumes.getItems()) {
//            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
//            Volume.SaleInfo saleInfo = volume.getSaleInfo();
//            System.out.println("==========");
//            // Title.
//            System.out.println("Title: " + volumeInfo.getTitle());
//            // Author(s).
//            java.util.List<String> authors = volumeInfo.getAuthors();
//            if (authors != null && !authors.isEmpty()) {
//                System.out.print("Author(s): ");
//                for (int i = 0; i < authors.size(); ++i) {
//                    System.out.print(authors.get(i));
//                    if (i < authors.size() - 1) {
//                        System.out.print(", ");
//                    }
//                }
//                System.out.println();
//            }
//            // Description (if any).
//            if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
//                System.out.println("Description: " + volumeInfo.getDescription());
//            }
//            // Ratings (if any).
//            if (volumeInfo.getRatingsCount() != null && volumeInfo.getRatingsCount() > 0) {
//                int fullRating = (int) Math.round(volumeInfo.getAverageRating().doubleValue());
//                System.out.print("User Rating: ");
//                for (int i = 0; i < fullRating; ++i) {
//                    System.out.print("*");
//                }
//                System.out.println(" (" + volumeInfo.getRatingsCount() + " rating(s))");
//            }
//            // Price (if any).
////            if (saleInfo != null && "FOR_SALE".equals(saleInfo.getSaleability())) {
////                double save = saleInfo.getListPrice().getAmount() - saleInfo.getRetailPrice().getAmount();
////                if (save > 0.0) {
////                    System.out.print("List: " + CURRENCY_FORMATTER.format(saleInfo.getListPrice().getAmount())
////                            + "  ");
////                }
////                System.out.print("Google eBooks Price: "
////                        + CURRENCY_FORMATTER.format(saleInfo.getRetailPrice().getAmount()));
////                if (save > 0.0) {
////                    System.out.print("  You Save: " + CURRENCY_FORMATTER.format(save) + " ("
////                            + PERCENT_FORMATTER.format(save / saleInfo.getListPrice().getAmount()) + ")");
////                }
////                System.out.println();
////            }
//            // Access status.
//            String accessViewStatus = volume.getAccessInfo().getAccessViewStatus();
//            String message = "Additional information about this book is available from Google eBooks at:";
//            if ("FULL_PUBLIC_DOMAIN".equals(accessViewStatus)) {
//                message = "This public domain book is available for free from Google eBooks at:";
//            } else if ("SAMPLE".equals(accessViewStatus)) {
//                message = "A preview of this book is available from Google eBooks at:";
//            }
//            System.out.println(message);
//            // Link to Google eBooks.
//            System.out.println(volumeInfo.getInfoLink());
//        }
//        System.out.println("==========");
//        System.out.println(
//                volumes.getTotalItems() + " total results at http://books.google.com/ebooks?q="
//                        + URLEncoder.encode(query, "UTF-8"));

    }

    static void errorIfNotSpecified() {
        if (API_KEY.startsWith("Enter ")) {
            System.err.println(API_KEY);
            System.exit(1);
        }
    }


}
