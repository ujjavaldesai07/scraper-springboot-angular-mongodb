package com.springboot.scraperservice.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the helper class to convert the date from string and vice versa.
 */
public class DateConversion {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(DateConversion.class));

    public static Date convertStrToDate(String dateStr, String dateFormat) {
        if (dateStr != null) {
            try {
                DateFormat format = new SimpleDateFormat(dateFormat);
                return format.parse(dateStr);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, String.format("Unable to parse date: %s, dateFormat = %s",
                        dateStr, dateFormat));
            }
        }

        return null;
    }

    public static String convertDateToStr(Date date, String dateFormat) {
        if (date != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                return simpleDateFormat.format(date.getTime());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, String.format("Unable to parse date: %s, dateFormat = %s",
                        date, dateFormat));
            }
        }

        return null;
    }
}
