/**
 *
 */
package edu.sjsu.cmpe275.lms.entity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "USER_BOOK")
public class UserBook {


    @EmbeddedId
    @Column(name = "ID")
    private UserBookId id;
    @ManyToOne
    @JoinColumn(name = "BOOK", insertable = false, updatable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "USER", insertable = false, updatable = false)
    private User user;
    @Column(name = "CHECKOUT_DATE")
    private String checkout_date;
    // 0 or 1 or 2
    @Column(name = "RENEW_FLAG")
    private Integer renew_flag;
    @Column(name = "FINE")
    private Integer fine;


    public UserBook() {

    }

    public UserBook(Book b, User u, LocalDateTime f, Integer renewFlag) {
        // create primary key
        this.id = new UserBookId(b.getBookId(), u.getId());

        // initialize attributes
        this.book = b;
        this.user = u;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.checkout_date = dtf.format(f);
        this.renew_flag = renewFlag;
        this.fine = 0;

    }

    public UserBookId getId() {
        return id;
    }

    public void setId(UserBookId id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public Integer getRenew_flag() {
        return renew_flag;
    }

    public void setRenew_flag(Integer renew_flag) {
        this.renew_flag = renew_flag;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public void UserBookPersist(Book b, User u) {
        u.getCurrentBooks().add(this);
        b.getCurrentUsers().add(this);

    }

    public String getDueDate() throws ParseException {

        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date duedate = dtf.parse(this.checkout_date);

        Calendar cal = new GregorianCalendar();
        cal.setTime(duedate);
        cal.add(Calendar.DATE, 30);

        String dueDate = dtf.format(cal.getTime());
        // System.out.println("String new due date " + dueDate);

        return dueDate;
    }

    public void setCalculateFine(Date currDate) throws ParseException {




        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date checkDate = dtf.parse(this.getDueDate());
        System.out.println("setCalculateFine: checkoutDate: " + this.checkout_date);
        System.out.println("setCalculateFine: getDueDate: " + this.getDueDate());
        //Date checkDate = new Date();
        // Date currDate = new Date();
        //LocalDate checkDate = dtf.parse(this.checkout_date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //LocalDate currDate = LocalDate.now();
        //Period p = Period.between(checkDate, currDate);
        long hours = (currDate.getTime() - checkDate.getTime()) / (60 * 60 * 1000);
        System.out.println("setCalculateFine: long hours: " + hours);
        if (hours <= 0) {
            this.fine = 0;
            return;
        }
        Integer intHours = (int) (long) hours;
        System.out.println("setCalculateFine: intHours: " + intHours);
        this.fine = ((intHours / 24) + 1);
    }

}
