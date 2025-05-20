package com.bookstore.server.core.util;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author davidgarcia
 */
public class DateUtil {


    public static String formatDate(LocalDate date) {
        String dateFormatted = null;
        FormatStyle dateStyle = FormatStyle.MEDIUM;

        if(date != null) {
            dateFormatted = date.format(DateTimeFormatter.ofLocalizedDate(dateStyle));
        }

        if(dateFormatted != null) {
            return dateFormatted;
        } else {
            throw new DateTimeException("The date could not be formatted.");
        }
    }

}
