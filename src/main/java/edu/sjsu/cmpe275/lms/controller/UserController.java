package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.service.BookService;
import edu.sjsu.cmpe275.lms.service.UserBookService;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
	 * @return
    //	 */
//	@RequestMapping(value = "/user", method = RequestMethod.GET)
//	public ModelAndView showUserCreationForm() {
//		ModelAndView modelAndView = new ModelAndView("users/userDashboard");
//		return modelAndView;
//	}
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
		ModelAndView mv = new ModelAndView("users/userDashboard");
		User uEntity = uService.createUser(sjsuid, useremail, password);
		String usertype = uEntity.getRole();
		System.out.println("Usertrpe " + usertype);
		mv.addObject("userId", uEntity.getId());
		if (usertype.equals("ROLE_LIBRARIAN")) {
			return "librarian/dashboard";
		}
		System.out.println("user id " + uEntity.getId());
		return mv;
	}


	@RequestMapping(value = "/user/{userId}/dashboard", method = RequestMethod.GET)
	public Object userDashboard(@PathVariable Integer userId,
								ModelMap model,
								HttpServletRequest request,
								HttpServletResponse response) {

		ModelAndView mv = new ModelAndView("users/userDashboard");
		mv.addObject("userId", userId);
		return mv;
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

//	@RequestMapping(value = "/user/{userId}/books", method = RequestMethod.GET)
//	public @ResponseBody List<Book> showBooksinDashboard(@PathVariable("userId") Integer userId,
//						 ModelMap model,
//						 HttpServletRequest request,
//						 HttpServletResponse response) {
//		//ModelAndView mv = new ModelAndView("books/userBookList");
//		List<Book> books = bService.listBooksOfUser(userId);
////		if(books.size()<1)
////			mv.addObject("errorMessage","No Books");
////		mv.addObject("userId",userId);
////		mv.addObject("books", books);
//		return books;
//	}

	@RequestMapping(value = "/user/{userId}/books/{bookId}", method = RequestMethod.GET)
	public Object requestBooks(@PathVariable("userId") Integer userId,
			@PathVariable("bookId") Integer bookId) throws ParseException {
		ModelAndView mv = new ModelAndView("books/request");
		List<Book> currBooks = bService.listBooksOfUser(userId);
		if (currBooks.size() > 9) {
            mv.addObject("status","Maximum 10 books can be issued at a time. Must return a book to issue new.");
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


		String status = bService.returnBook(bookId,userId);

		if(status.equalsIgnoreCase("invalid book")){
			mv.setViewName("books/request");
			mv.addObject("userId", userId);
			mv.addObject("status",status);
			return mv;
		}
		mv.addObject("userId", userId);
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
	public ModelAndView searchBookbyUser(ModelAndView modelAndView,
										 @PathVariable("userId") Integer userId,
										 @RequestParam(value = "isbn", required = false) String isbn,
										 @RequestParam(value = "author", required = false) String author,
										 @RequestParam(value = "publisher", required = false) String publisher,
										 @RequestParam(value = "year_of_publication", required = false) String year_of_publication,
										 @RequestParam(value = "num_of_copies", required = false) String num_of_copies,
										 @RequestParam(value = "callnumber", required = false) String callnumber,
										 @RequestParam(value = "current_status", required = false) String current_status,
										 @RequestParam(value = "keywords", required = false) String keywords
	) {
		System.out.println("HEREEEEEEEEEEE!!!!!!!!!!");
		Book book = new Book();
		if (isbn != null && !isbn.isEmpty()) {
			book.setIsbn(isbn);
		}
		if (author != null && !author.isEmpty()) {
			book.setAuthor(author);
		}
		if (publisher != null && !publisher.isEmpty()) {
			book.setPublisher(publisher);
		}
		if (year_of_publication != null && !year_of_publication.isEmpty()) {
			book.setYear_of_publication(year_of_publication);
		}
		if (num_of_copies != null && !num_of_copies.isEmpty()) {
			book.setNum_of_copies(Integer.parseInt(num_of_copies));
		}
		if (current_status != null && !current_status.isEmpty()) {
			book.setCurrent_status(current_status);
		}
		if (keywords != null && !keywords.isEmpty()) {
			book.setKeywords(keywords);
		}
		if ((book.getIsbn() == null || book.getIsbn().isEmpty()) && (book.getAuthor() == null || book.getAuthor().isEmpty()) && (book.getTitle() == null || book.getTitle().isEmpty()) && (book.getCallnumber() == null || book.getCallnumber().isEmpty()) && (book.getPublisher() == null || book.getPublisher().isEmpty()) && (book.getYear_of_publication() == null || book.getYear_of_publication().isEmpty()) && (book.getCurrent_status() == null || book.getCurrent_status().isEmpty())) {
			modelAndView.setViewName("books/searchBook");
			modelAndView.addObject("errorMessage", "At least one search criteria is mandatory");
			return modelAndView;
		}
		modelAndView.setViewName("books/listBooks");
		List<Book> books = bService.searchBookbyUser(book);

		if (books.isEmpty()) modelAndView.addObject("errorMessage", "Sorry, no books matching search criteria found.");
		modelAndView.addObject("books", books);
		modelAndView.addObject("userId", userId);
		return modelAndView;
	}


    @RequestMapping(value = "/user/searchBook", method = RequestMethod.POST)
    @Transactional
    public ModelAndView searchBook(ModelAndView modelAndView,
                                   @RequestParam(value = "isbn", required = false) String isbn,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "publisher", required = false) String publisher,
                                   @RequestParam(value = "year_of_publication", required = false) String year_of_publication,
                                   @RequestParam(value = "num_of_copies", required = false) String num_of_copies,
                                   @RequestParam(value = "callnumber", required = false) String callnumber,
                                   @RequestParam(value = "current_status", required = false) String current_status,
                                   @RequestParam(value = "keywords", required = false) String keywords
    ) {
        System.out.println("HEREEEEEEEEEEE!!!!!!!!!!");
        Book book = new Book();
        if (isbn != null && !isbn.isEmpty()) {
            book.setIsbn(isbn);
        }
        if (author != null && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if (publisher != null && !publisher.isEmpty()) {
            book.setPublisher(publisher);
        }
        if (year_of_publication != null && !year_of_publication.isEmpty()) {
            book.setYear_of_publication(year_of_publication);
        }
        if (num_of_copies != null && !num_of_copies.isEmpty()) {
            book.setNum_of_copies(Integer.parseInt(num_of_copies));
        }
        if (current_status != null && !current_status.isEmpty()) {
            book.setCurrent_status(current_status);
        }
        if (keywords != null && !keywords.isEmpty()) {
            book.setKeywords(keywords);
        }
        if ((book.getIsbn() == null || book.getIsbn().isEmpty()) && (book.getAuthor() == null || book.getAuthor().isEmpty()) && (book.getTitle() == null || book.getTitle().isEmpty()) && (book.getCallnumber() == null || book.getCallnumber().isEmpty()) && (book.getPublisher() == null || book.getPublisher().isEmpty()) && (book.getYear_of_publication() == null || book.getYear_of_publication().isEmpty()) && (book.getCurrent_status() == null || book.getCurrent_status().isEmpty())) {
            modelAndView.setViewName("books/searchBook");
            modelAndView.addObject("errorMessage", "At least one search criteria is mandatory");
			return modelAndView;
		}
		modelAndView.setViewName("books/listBooks");
		List<Book> books = bService.searchBookbyUser(book);

		if (books.isEmpty()) modelAndView.addObject("errorMessage", "Sorry, no books matching search criteria found.");
		modelAndView.addObject("books", books);
		return modelAndView;
	}
}

