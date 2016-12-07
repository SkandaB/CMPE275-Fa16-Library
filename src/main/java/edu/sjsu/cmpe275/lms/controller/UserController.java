package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserBookCart;
import edu.sjsu.cmpe275.lms.errors.Err;
import edu.sjsu.cmpe275.lms.service.BookService;
import edu.sjsu.cmpe275.lms.service.UserBookCartService;
import edu.sjsu.cmpe275.lms.service.UserBookService;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

@Controller
@Transactional
public class UserController {
	@Autowired
	UserService uService;
	@Autowired
	BookService bService;
    @Autowired
    UserBookService ubService;
    @Autowired
    UserBookCartService ubcService;

	/**
	 * @return
//	 */
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
        System.out.println("Usertrpe "+usertype);
        if (usertype.equals("ROLE_LIBRARIAN")){
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
		if(books.size()<1)
			mv.addObject("errorMessage","No Books");
		mv.addObject("userId",userId);
		mv.addObject("books", books);
		return mv;
	}

//    @RequestMapping(value = "/user/{userId}/clearcart", method = RequestMethod.DELETE)
//    public Object clearUserCart(@PathVariable("userId") Integer userId, ModelAndView modelAndView) {
//        Err err = ubcService.addUserBookToCart(new UserBookCart(userId, bookId));
//        String addToCartStatus;
//        if (err.isAnError()) {
//            addToCartStatus = err.getMessage();
//
//        } else {
//            addToCartStatus = "Book added to cart";
//        }
//        modelAndView.addObject("addtocartstatus", addToCartStatus);
//        modelAndView.setViewName("test/addtocart");
//        return modelAndView;
//    }

    @RequestMapping(value = "/user/{userId}/books/{bookId}", method = RequestMethod.GET)
    public Object addBookToUserCart(@PathVariable("userId") Integer userId,
                               @PathVariable("bookId") Integer bookId, ModelAndView modelAndView) throws ParseException {
        Err err = ubcService.addUserBookToCart(new UserBookCart(userId, bookId));
        String addToCartStatus;
        if (err.isAnError()) {
            addToCartStatus = err.getMessage();

        } else {
            addToCartStatus = "Book added to cart";
        }
        modelAndView.addObject("addtocartstatus", addToCartStatus);
        modelAndView.setViewName("test/addtocart");
        return modelAndView;
    }

	@RequestMapping(value = "/user/{userId}/checkout", method = RequestMethod.GET)
	public Object requestBooks(@PathVariable("userId") Integer userId) throws ParseException {
		ModelAndView mv = new ModelAndView("books/request");
        List<UserBookCart> cart = ubcService.getUserCart(userId);
        if (cart.size() == 0) {
            mv.addObject("status","Cart is Empty. Nothing to checkout");
            return mv;
        }
		List<Book> currBooks = bService.listBooksOfUser(userId);
		if (currBooks.size() + cart.size()> 10) {
            mv.addObject("status","Maximum 10 books can be issued at a time. Must return a book or remove from cart to issue new.");
            return mv;
        }

        int userDayBookCount = ubService.getUserDayBookCount(userId);
        if (userDayBookCount + cart.size() > 5) {
            mv.addObject("status", "Maximum 5 books can be issued in a day. Must return a book or remove from cart today or try tomorrow");
            return mv;
        }

        for (UserBookCart u : cart) {
            bService.requestBook(u.getBook_id(), userId);
        }

        mv.addObject("status", "Books checked out! You will get details in email soon !");
        ubcService.clearUserCart(userId);
        return mv;
    }

	@RequestMapping(value = "/user/{userId}/book/{bookId}", method = RequestMethod.GET)
	public Object returnBook(@PathVariable("userId") Integer userId,
							   @PathVariable("bookId") Integer bookId) throws ParseException {
		ModelAndView mv = new ModelAndView("books/userBookList");


		String status = bService.returnBook(bookId,userId);

		if(status.equalsIgnoreCase("invalid book")){
			mv.setViewName("books/request");
			mv.addObject("status",status);
			return mv;
		}
		mv.addObject("status",status);
		return mv;
	}

	@Transactional
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ModelAndView searchBookPage(ModelAndView modelAndView) {
		modelAndView.setViewName("books/searchBook");
		modelAndView.addObject("book", new Book());
		return modelAndView;
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
	@Transactional
	public ModelAndView searchBook(@PathVariable("userId") Integer userId,
								   @ModelAttribute("book") Book book,
								   ModelAndView modelAndView) {
		if ((book.getIsbn() == null || book.getIsbn().isEmpty()) && (book.getAuthor() == null || book.getAuthor().isEmpty()) && (book.getTitle() == null || book.getTitle().isEmpty()) && (book.getCallnumber() == null || book.getCallnumber().isEmpty()) && (book.getPublisher() == null || book.getPublisher().isEmpty()) && (book.getYear_of_publication() == null || book.getYear_of_publication().isEmpty()) && (book.getCurrent_status() == null || book.getCurrent_status().isEmpty())) {
			modelAndView.setViewName("books/searchBook");
			modelAndView.addObject("errorMessage", "At least one search criteria is mandatory");
			return modelAndView;
		}
		modelAndView.addObject("userId",userId);
		modelAndView.setViewName("books/listBooks");
		List<Book> books = bService.searchBookbyUser(book);

		if (books.isEmpty()) modelAndView.addObject("errorMessage", "Sorry, no books matching search criteria found.");
		modelAndView.addObject("books", books);
		return modelAndView;
	}
}

