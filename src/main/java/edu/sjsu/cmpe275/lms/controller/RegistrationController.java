package edu.sjsu.cmpe275.lms.controller;

import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.entity.UserVerfToken;
import edu.sjsu.cmpe275.lms.registration.RegistrationCompleteEvent;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showUserCreationForm() {
        ModelAndView modelAndView = new ModelAndView("users/addUser");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerNewUserAccount(
            @RequestParam long sjsuid,
            @RequestParam String useremail,
            @RequestParam String password,
            HttpServletRequest request) {
        System.out.println("************* Received the following from the form: SJSUID: " + sjsuid + " UserEmail: " + useremail + " Password: " + password);

        User added = uService.createUser(sjsuid, useremail, password);
        System.out.println("************* The following user was added into the database: " + added.toString());
        if (added == null) {
            System.out.println("*********** Calling Error Page");
            return new ModelAndView("error");
        }
        try {
            System.out.println("************* User addition was successful, so entered the event creator *************");
            String url = request.getRequestURL().toString();
            //System.out.println("************* Context Path URL: "+ url);
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(added, url));
        } catch (Exception e) {
            System.out.println(e);
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("errorMessage", e);
            return mv;
        }
        ModelAndView mv = new ModelAndView("users/welcome");
        mv.addObject("message", "User with email: " + added.getUseremail() + " successfully created! \r\n Please check your inbox and validate your account to use full user services.");
        return mv;
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
        modelAndView.addObject("message", "Success! \r\n" + user.getUseremail() + " validation successful. You may now use the full user services ");
        return modelAndView;
    }
}
