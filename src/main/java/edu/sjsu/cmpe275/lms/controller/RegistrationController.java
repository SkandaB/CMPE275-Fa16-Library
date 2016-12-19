package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserVerfToken;
import edu.sjsu.cmpe275.lms.registration.RegistrationCompleteEvent;
import edu.sjsu.cmpe275.lms.service.UserService;
import edu.sjsu.cmpe275.lms.time.ClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
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
import java.text.ParseException;


/**
 * The Registration Controller for user registration and login in LMS.
 */
@Component
@Controller
@EnableAspectJAutoProxy
@EnableScheduling
public class RegistrationController {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService uService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    ClockService clockService;

    @RequestMapping(value = "/")
    public ModelAndView landingPage() {
        clockService.displayCurrentTime();
        return new ModelAndView("redirect:/register");
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showUserCreationForm(HttpServletRequest request) {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("users/addUser");
        modelAndView.addObject("userForm", user);
        return modelAndView;
    }

    /**
     * @param request
     * @param user
     * @param bindingResult
     * @param response
     * @return The user dashboard based on the role.
     */
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

    /**
     *
     * @param request
     * @param user
     * @param bindingResult
     * @return Gets the dashboard.
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView showDashBoard(HttpServletRequest request,
                                      @Valid @ModelAttribute("loginForm") User user,
                                      BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        try {
            User us = (User) request.getSession().getAttribute("user");
            if (us.getRole().equals("ROLE_LIBRARIAN")) {
                System.out.println("Lib found");
                mv.setViewName("librarian/dashboard");
                mv.addObject("users", user);
                /*mv.addObject("custom_clock",clockService.getCalendar());*/
            } else if (us.getRole().equals("ROLE_PATRON")) {
                System.out.println("patron found");
                mv.setViewName("user/dashboard");
                mv.addObject("users", user);
            } else {
                return new ModelAndView("redirect:/register");
            }
            return mv;
        } catch (Exception e) {
            return new ModelAndView("redirect:/register");
        }

        /*ModelAndView mv;
        User loggedInUser = uService.findUserByEmail(user.getUseremail());
        if (loggedInUser == null || !user.getPassword().equals(loggedInUser.getPassword()) || !loggedInUser.isEnabled()) {
            mv = new ModelAndView("error");
            mv.addObject("errorMessage", "Bad Credentials. No user found with this email/password combination. \r\n Is your account validation pending? If so, please check inbox and re-validate account.");
            return new ModelAndView(new RedirectView("/register"));
        } else {
            if (loggedInUser.getRole().equalsIgnoreCase("ROLE_PATRON")) {

                mv = new ModelAndView("users/userDashboard");
                mv.addObject("userId", loggedInUser.getId());
                return mv;
            } else {

                mv = new ModelAndView("librarian/dashboard");
            }
            request.getSession().setAttribute("user", loggedInUser);
            mv.addObject("users", user);
            return mv;
        }*/

    }

    /**
     * @param request
     * @return homepage
     */
    @RequestMapping(value = "/lmsdashboard", method = RequestMethod.GET)
    public ModelAndView showlmsdashboard(HttpServletRequest request) {
        /*ModelAndView mv;
        mv = new ModelAndView("librarian/dashboard");
        return mv;*/
        return new ModelAndView("redirect:/dashboard");
    }

    /**
     *
     * @param newdatestr
     * @return set the custom date.
     * @throws ParseException
     */
    @RequestMapping(value = "/dashboard/changedate", method = RequestMethod.POST)
    public ModelAndView setCustomDate(@RequestParam(value = "newdate") String newdatestr) throws ParseException {
        System.out.println("Date returned from the webpage = " + newdatestr);
        if (newdatestr.isEmpty()) {
            clockService.resetCalendar();
        } else {
            clockService.setCalendar(newdatestr);
        }
        System.out.print("!!!!!!!!!!!!!!!!!!!! Displaying the system set app time: ");
        clockService.displayCurrentTime();
        return new ModelAndView("redirect:/dashboard");
    }

    /**
     * @param request
     * @param user
     * @param bindingResult
     * @return the dashboard for the user to login
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request,
                                  @Valid @ModelAttribute("loginForm") User user,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("******** Result has errors: ******");
            return new ModelAndView("users/addUser");
        }

        User loggedInUser = uService.findUserByEmail(user.getUseremail());
        ModelAndView mv;
        if (loggedInUser == null || !user.getPassword().equals(loggedInUser.getPassword()) || !loggedInUser.isEnabled()) {
            mv = new ModelAndView("error");
            mv.addObject("errorMessage", "Bad Credentials. No user found with this email/password combination. \r\n Is your account validation pending? If so, please check inbox and re-validate account.");
            return mv;
        } else {
            if (loggedInUser.getRole().equalsIgnoreCase("ROLE_PATRON")) {

                mv = new ModelAndView("users/userDashboard");
                mv.addObject("users", user);
                mv.addObject("userId", loggedInUser.getId());
                return mv;
            } else {
                mv = new ModelAndView("librarian/dashboard");
                mv.addObject("users", user);
            }
            request.getSession().setAttribute("user", loggedInUser);

            return mv;
        }
    }

    /**
     * @param token
     * @return The view to confirm registered token
     */
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
        modelAndView.addObject("showsignin", "true");
        modelAndView.addObject("message", "Success! \r\n" + user.getUseremail() + " validation successful. You may now use the full user services ");
        return modelAndView;
    }

    /**
     * @param request
     * @return The register page
     */
    @RequestMapping(value = "/logoutuser", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request) {
        System.out.println("Logout called !!");
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/";
    }
}
