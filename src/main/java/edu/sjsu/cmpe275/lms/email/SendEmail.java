package edu.sjsu.cmpe275.lms.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by SkandaBhargav on 12/5/16.
 */
@Service
public class SendEmail {
    @Autowired
    private MailSender mailSender;

    public void sendMail(String toAddress, String emailSubject, String emailMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("group2lmscmpe275@gmail.com");
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(emailMessage);
        mailSender.send(mailMessage);
    }
}
