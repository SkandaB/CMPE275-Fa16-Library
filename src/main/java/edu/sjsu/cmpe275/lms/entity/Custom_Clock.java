package edu.sjsu.cmpe275.lms.entity;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SkandaBhargav on 12/16/16.
 */
@Component("Custom_Clock")
public class Custom_Clock {
    Calendar calendar = new GregorianCalendar();


    public Custom_Clock() {
        calendar = Calendar.getInstance();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd hh:mm");
        //String dateInString = "2016/12/26 18:24";
        Date date = sdf.parse(dateStr);

        calendar.setTime(date);
    }

    public void resetCalendar() {
        calendar = Calendar.getInstance();
    }
}