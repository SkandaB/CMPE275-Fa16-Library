package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.dao.BookDao;
import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserVerfToken;
import edu.sjsu.cmpe275.lms.registration.RegistrationCompleteEvent;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by SkandaBhargav on 11/28/16.
 */
@Controller
public class RegistrationController {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService uService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    private BookDao bookDao;

 /*   @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView landing() {
        return new ModelAndView(new RedirectView("/register"));
    }*/


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showUserCreationForm(HttpServletRequest request/*, Map<String,Object> model*/) {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("users/addUser");
        modelAndView.addObject("userForm", user);
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerNewUserAccount(
            HttpServletRequest request,
            @Valid @ModelAttribute("userForm") User user,
            BindingResult bindingResult,
            final HttpServletResponse response) {
        System.out.println("************* Received the following from the form: SJSUID: " + user.getSjsuid() + " UserEmail: " + user.getUseremail() + " Password: " + user.getPassword());

        if (bindingResult.hasErrors()) {
            System.out.println("******** Result has errors: ******");
            return new ModelAndView("users/addUser");
        }

        try {
            User added = uService.createUser(user.getSjsuid(), user.getUseremail(), user.getPassword());
            System.out.println("************* The following user will be added into the database: " + added.toString());
            if (added == null) {
                String errorMessage = "Error creating user in database";
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                System.out.println("*********** Calling Error Page " + errorMessage);
                ModelAndView mv = new ModelAndView("error");
                mv.addObject("responseCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                mv.addObject("errorMessage", errorMessage);
                return mv;
            }
            try {
                System.out.println("************* User addition was successful, so entered the event creator *************");
                String url = request.getRequestURL().toString();
                System.out.println("************* Request URL: " + url + "ContextPath= " + request.getContextPath().toString());

                applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(added, url));
            } catch (Exception e) {
                System.out.println(e);
                ModelAndView mv = new ModelAndView("error");
                mv.addObject("errorMessage", e);
                return mv;
            }
            ModelAndView mv = new ModelAndView("users/welcome");
            mv.addObject("message", "User with email: " + added.getUseremail() + " successfully created! \r\nPlease check your inbox and validate your account to use full user services.");
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Duplicate user in database";
            response.setStatus(HttpStatus.CONFLICT.value());
            System.out.println("*********** Calling Error Page " + errorMessage);
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("responseCode", HttpStatus.CONFLICT.value());
            mv.addObject("errorMessage", errorMessage);
            return mv;
        }
    }


    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request,
                                  @Valid @ModelAttribute("loginForm") User user,
                                  BindingResult bindingResult) {
        System.out.println("Details from Login form: " + user.toString());
        User loggedInUser = uService.findUserByEmail(user.getUseremail());
        System.out.println("Logged in User from DB" + loggedInUser);
        ModelAndView mv;
        if (loggedInUser == null) {
            mv = new ModelAndView("error");
            mv.addObject("errorMessage", "Bad Credentials. No user found with this email/password combination.");
            return mv;
        } else {
            mv =  new ModelAndView("librarian/dashboard");

//           int count =  bookDao.findCountAvailable();
//            System.out.println("count  : "+count);
            mv.addObject("users",user);
            return mv;
        }
    }

    @RequestMapping(value = "/register/confirmRegistration.html", method = RequestMethod.GET)
    public ModelAndView confirmRegisteredAccount(@RequestParam("token") String token) {
        System.out.println("*********** Token from URL = " + token);
        UserVerfToken userVerfToken = uService.getUserToken(token);
        if (userVerfToken == null) {
            return new ModelAndView(new RedirectView("redirect:/error"));
        }

        User user = userVerfToken.getUser();
        user.setEnabled(true);
        uService.saveValidatedUser(user);
        ModelAndView modelAndView = new ModelAndView("users/welcome");
        modelAndView.addObject("showsignin" , "true");
        modelAndView.addObject("message", "Success! \r\n" + user.getUseremail() + " validation successful. You may now use the full user services ");
        return modelAndView;
    }

    @RequestMapping(value="/logoutuser", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request) {
        System.out.println("Logout called !!");
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
            return "redirect:/";
        }
    }

