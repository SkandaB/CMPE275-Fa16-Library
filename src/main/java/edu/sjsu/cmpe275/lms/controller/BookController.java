package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {

    @Autowired
    private BookDao bookDao;

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public ModelAndView getAddBookPage() {
        ModelAndView modelAndView = new ModelAndView("addBook");
        return modelAndView;
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@RequestParam("isbn") String isbn, @RequestParam("image") MultipartFile file) {

        if (file.isEmpty()) return "Failed to upload because the file was empty.";
        try {
            byte[] bytes = file.getBytes();
            System.out.println(bytes);

            bookDao.addBook(isbn, "xx", "xx", "xx", "xx", 2009, "xx", 5, "xx", "xx", bytes);

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

            return "Successfully uploaded file";
        } catch (Exception e) {
            return "Failed to upload => " + e.getMessage();
        }
    }
}