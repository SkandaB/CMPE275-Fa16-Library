package edu.sjsu.cmpe275.lms.registration.listener;

import edu.sjsu.cmpe275.lms.entity.User;
import edu.sjsu.cmpe275.lms.registration.RegistrationCompleteEvent;
import edu.sjsu.cmpe275.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Created by SkandaBhargav on 11/28/16.
 */
@Component
@Service
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        this.confirmRegistration(registrationCompleteEvent);
    }

    private void confirmRegistration(RegistrationCompleteEvent registrationCompleteEvent) {
        User user = registrationCompleteEvent.getUser();
        String token = UUID.randomUUID().toString();

        userService.createToken(user, token);

        String toAddress = user.getUseremail();
        String emailSubject = "Please validate your Account";
        String clickURL = registrationCompleteEvent.getUrl() + "/confirmRegistration.html?token=" + token;
        String emailMessage = "Registration successful. Next Step \r\n" + clickURL;

        System.out.println("**************** The following email will be sent: " + emailMessage);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("group2lmscmpe275@gmail.com");
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(emailMessage);


        mailSender.send(mailMessage);


    }

    /*private SimpleMailMessage writeEmailMessage(RegistrationCompleteEvent registrationCompleteEvent, User user, String token) {
        String toAddress = user.getUseremail();
        String emailSubject = "Please validate your Account";
        String clickURL = registrationCompleteEvent.getUrl() + "/confirmRegistration.html?token=" + token;
        String emailMessage = "Registration successful. Next Step";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(emailMessage + "\r\n" + clickURL);
        mailMessage.setFrom("Library Management Admin");
        System.out.println("********* Composed email message! *********");
        return mailMessage;
    }*/

}
