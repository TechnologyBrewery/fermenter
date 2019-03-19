package org.bitbucket.fermenter.stout.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateTestUtils {
	
    private static ThreadLocal<DateFormat> threadLocalDateFormat = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy/MM/dd"));

    private DateTestUtils() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTestUtils.class);

    /**
     * Helper method for getting date from date string
     * Using format yyyy-MM-dd.
     * 
     * @param date string
     * @return date
     */
    public static Date convertToDate(String dateStr) {
        Date date = null;
        DateFormat formatter = threadLocalDateFormat.get();
        if (StringUtils.equalsIgnoreCase("today", StringUtils.trim(dateStr))) {
            date = getRelativeDate(0);
        } else if (StringUtils.equalsIgnoreCase("tomorrow", StringUtils.trim(dateStr))) {
            date = getRelativeDate(1);
        } else if (StringUtils.equalsIgnoreCase("yesterday", StringUtils.trim(dateStr))) {
            date = getRelativeDate(-1);
        } else if (StringUtils.isNotBlank(dateStr)) {
            try {
                if (StringUtils.isNotEmpty(dateStr)) {
                    date = formatter.parse(dateStr);
                }
            } catch (ParseException e) {
                LOGGER.error("Error parsing date string: " + dateStr, e.getMessage());
            }
        }
        return date;
    }

    /**
     * Helper method for getting relative dates.
     * 
     * @param relativeDate
     * @return date
     */
    private static Date getRelativeDate(int relativeDate) {
        // today
        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Date testDate = DateUtils.addDays(date.getTime(), relativeDate);
        return testDate;
    }
}
