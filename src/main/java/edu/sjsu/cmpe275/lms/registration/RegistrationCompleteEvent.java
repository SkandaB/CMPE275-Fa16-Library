package edu.sjsu.cmpe275.lms.registration;

import edu.sjsu.cmpe275.lms.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by SkandaBhargav on 11/28/16.
 */

public class RegistrationCompleteEvent extends ApplicationEvent {
    private final String url;
    private final User user;

    /**
     * @param user
     * @param url
     */
    public RegistrationCompleteEvent(User user, String url) {
        super(user);
        this.url = url;
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }
}
