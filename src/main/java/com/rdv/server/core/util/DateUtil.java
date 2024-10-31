package com.rdv.server.core.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author davidgarcia
 */
public class DateUtil {


    public static String formatDate(LocalDate date, Locale locale) {
        String dateFormatted = null;
        FormatStyle dateStyle = FormatStyle.MEDIUM;

        if(date != null) {
            dateFormatted = date.format(DateTimeFormatter.ofLocalizedDate(dateStyle).withLocale(locale));
        }

        if(dateFormatted != null) {
            return dateFormatted;
        } else {
            throw new DateTimeException("The date could not be formatted.");
        }
    }

    public static String formatDateWithDayAndMonth(LocalDate date, Locale locale) {
        String dateFormatted = null;
        if(date != null) {
            dateFormatted = date.format(DateTimeFormatter.ofLocalizedPattern("dd MMM").withLocale(locale));
        }

        if(dateFormatted != null) {
            return dateFormatted;
        } else {
            throw new DateTimeException("The date (day and month) could not be formatted.");
        }
    }

    public static String formatDateAndTime(OffsetDateTime dateAndTime, Locale locale) {
        String dateAndTimeFormatted = null;
        if(dateAndTime != null) {

            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;

            dateAndTimeFormatted = dateAndTime.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
                    .withZone(ZoneId.of("America/Montreal")).withLocale(locale));
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

            dateAndTimeFormatted = dateAndTime.format(DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
                    .withZone(ZoneId.of("America/Montreal")).withLocale(locale));
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
