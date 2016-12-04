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

@Controller
@RequestMapping("/book")
public class BookController {

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

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@ModelAttribute("book") Book book, @RequestParam("imagefile") MultipartFile imagefile, ModelAndView modelAndView) {
        //@RequestParam("isbn") String isbn, @RequestParam("author") String author, @RequestParam("title") String title, @RequestParam("callnumber") String callnumber, @RequestParam("publisher") String publisher, @RequestParam("year_of_publication") int year_of_publication, @RequestParam("location") String location, @RequestParam("num_of_copies") String num_of_copies, @RequestParam("keywords") String keywords,
      //  if (imagefile.isEmpty()) return "Failed to upload because the file was empty.";
        try {
            byte[] bytes = imagefile.getBytes();
            //System.out.println(bytes);

            book.setImage(bytes);
            //bookDao.addBook(isbn, "xx", "xx", "xx", "xx", 2009, "xx", 5, "xx", "xx", bytes);
            bookDao.addBook(book);

            /*// Creating the directory to store file
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "tmpFiles");
            if (!dir.exists()) dir.mkdirs();

            // Create the file on server
            File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            System.out.println("Server File Location="
                    + serverFile.getAbsolutePath());*/
            modelAndView.addObject("book", new Book());
            return "Successfully uploaded file";
        } catch (Exception e) {
            return "Failed to upload => " + e.getMessage();
        }
    }
    
    
    
    
}