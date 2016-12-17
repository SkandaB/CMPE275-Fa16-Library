package edu.sjsu.cmpe275.lms.time;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by SkandaBhargav on 12/16/16.
 */
@Component("ClockService")
public interface ClockService {

    Calendar getCalendar();

    void setCalendar(String dateStr) throws ParseException;

    void resetCalendar();

    void displayCurrentTime();
}
