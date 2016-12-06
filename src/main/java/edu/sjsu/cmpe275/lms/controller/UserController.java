package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.service.BookService;
import edu.sjsu.cmpe275.lms.service.UserBookService;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService uService;
    @Autowired
    BookService bService;
    @Autowired
    UserBookService ubService;

    /**
     * @return //
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView showUserCreationForm() {
        ModelAndView modelAndView = new ModelAndView("users/dashboard_user");
        return modelAndView;
    }
/*
* Comment from new branch Dhanya's Mac
* /
 */


/*	*/

    /**
     * @param sjsuid
     * @param useremail
     * @param password
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Object userCreating(@RequestParam long sjsuid,
                               @RequestParam String useremail,
                               @RequestParam String password,
                               ModelMap model,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("users/dashboard_user");
        User uEntity = uService.createUser(sjsuid, useremail, password);
        String usertype = uEntity.getRole();
        System.out.println("Usertrpe " + usertype);
        if (usertype.equals("ROLE_LIBRARIAN")) {
            return "librarian/dashboard";
        }
        return "users/welcome";
    }


    @RequestMapping(value = "/user/showall", method = RequestMethod.GET)
    public Object showAll() {
        ModelAndView mv = new ModelAndView("users/list");
        List<User> users = uService.listUsers();
        System.out.println(users.get(0).toString());
        mv.addObject("users", users);
        return mv;
    }

    @RequestMapping(value = "/user/{userId}/books", method = RequestMethod.GET)
    public Object showBooks(@PathVariable("userId") Integer userId,
                            ModelMap model,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("books/userBookList");
        List<Book> books = bService.listBooksOfUser(userId);
        if (books.size() < 1)
            mv.addObject("errorMessage", "No Books");
        mv.addObject("userId", userId);
        mv.addObject("books", books);
        return mv;
    }

    @RequestMapping(value = "/user/{userId}/books/{bookId}", method = RequestMethod.GET)
    public Object requestBooks(@PathVariable("userId") Integer userId,
                               @PathVariable("bookId") Integer bookId) throws ParseException {
        ModelAndView mv = new ModelAndView("books/request");
        List<Book> currBooks = bService.listBooksOfUser(userId);
        if (currBooks.size() > 9) {
            mv.addObject("status", "Maximum 10 books can be issued at a time. Must return a book to issue new.");
            return mv;
        }

        int userDayBookCount = ubService.getUserDayBookCount(userId);
        if (userDayBookCount > 4) {
            mv.addObject("status", "Maximum 5 books can be issued in a day. Must return a book today or try tomorrow");
            return mv;
        }

        String status = bService.requestBook(bookId, userId);
        mv.addObject("status", status);
        return mv;
    }

    @RequestMapping(value = "/user/{userId}/book/{bookId}", method = RequestMethod.GET)
    public Object returnBook(@PathVariable("userId") Integer userId,
                             @PathVariable("bookId") Integer bookId) throws ParseException {
        ModelAndView mv = new ModelAndView("books/userBookList");


        bService.returnBook(bookId, userId);
        //mv.addObject("status",status);
        return mv;
    }
}

