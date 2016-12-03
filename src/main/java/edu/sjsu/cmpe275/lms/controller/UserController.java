package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.Book;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.service.BookService;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
	@Autowired
	UserService uService;
	@Autowired
	BookService bService;

	/**
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView showUserCreationForm() {
		ModelAndView modelAndView = new ModelAndView("users/addUser");
		return modelAndView;
	}

	/**
	 * @param sjsuid
	 * @param useremail
	 * @param password
	 * @param role
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public Object userCreating(@RequestParam long sjsuid,
			@RequestParam String useremail,
			@RequestParam String password,
			@RequestParam String role,
			ModelMap model,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("users/addUser");
		User uEntity = uService.createUser(sjsuid, useremail, password, role);

		System.out.println(uEntity.toString());


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

	@RequestMapping(value = "/user/books", method = RequestMethod.GET)
	public Object showBooks() {
		ModelAndView mv = new ModelAndView("books/listBooks");
		List<Book> books = bService.listBooks();
		System.out.println(books.get(0).toString());
		mv.addObject("books", books);
		return mv;
	}

	@RequestMapping(value = "/user/{userId}/books/{bookId}", method = RequestMethod.GET)
	public Object requestBooks(@PathVariable("userId") Integer userId,
			@PathVariable("bookId") Integer bookId,
			ModelMap model,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("books/request");
		
		Book book = bService.findBookById(bookId);
		
		User user = uService.findUser(userId);
	
//		List<User> userlist = new ArrayList<User>();
//		userlist.add(user);
		String status = bService.requestBook(bookId,userId);
		mv.addObject("status",status);
		
		return mv;
	}
}

