package com.rdv.server.core.util;


import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author davidgarcia
 */
public class DateUtil {

    public static String formatDateAndTime(OffsetDateTime dateAndTime, Locale locale) {//
        String dateAndTimeFormatted = null;
        if(dateAndTime != null) {

            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;

            dateAndTimeFormatted = dateAndTime.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale));
        }

        if(dateAndTimeFormatted != null) {
            return dateAndTimeFormatted;
        } else {
            throw new DateTimeException("The date and time could not be formatted.");
        }
    }

    public static String formatLongDateAndTime(OffsetDateTime dateAndTime, Locale locale) {
        String dateAndTimeFormatted = null;
        if(dateAndTime != null) {

            FormatStyle dateStyle = FormatStyle.FULL;
            FormatStyle timeStyle = FormatStyle.SHORT;

            dateAndTimeFormatted = dateAndTime.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale));
        }

        if(dateAndTimeFormatted != null) {
            return capitaliseDateFirstLetter(dateAndTimeFormatted);
        } else {
            throw new DateTimeException("The date and time could not be formatted.");
        }
    }

    public static String capitaliseDateFirstLetter(String name){
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
