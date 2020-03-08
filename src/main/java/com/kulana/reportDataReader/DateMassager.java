package com.kulana.reportDataReader;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateMassager {
    private static final String formatterDateTime = "EEE MMM d HH:mm:ss zzz yyyy";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterDateTime, Locale.ENGLISH);

    public static LocalDateTime convertTextToLocalDateTime(String localTimeText) {
        LocalDateTime dateTime = LocalDateTime.parse(localTimeText, formatter);
        return dateTime;
    }

    public static boolean compareDate(LocalDateTime reportingDateTime, LocalDateTime currentRowDateTime, boolean isBefore) {
        if (isBefore) {
            return currentRowDateTime.isBefore(reportingDateTime);
        } else {
            return currentRowDateTime.isAfter(reportingDateTime);
        }
    }

    public static LocalDateTime getLocalDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dateTime = zonedDateTime.format(formatter);
        // System.out.println("Date time zone :" + dateTime);
        return DateMassager.convertTextToLocalDateTime(dateTime);
    }

    public static LocalDateTime createCustomDateTimeFromHrAndMin(String afterDateTime) {
        String[] arrs = afterDateTime.split(":");
        LocalDateTime localDateTime = DateMassager.getLocalDateTime();

        localDateTime = LocalDateTime.of(localDateTime.getYear(),
                localDateTime.getMonth(),
                localDateTime.getDayOfMonth(),
                Integer.valueOf(arrs[0]),
                Integer.valueOf(arrs[1]), 0);
        return localDateTime;
    }

    public static LocalDateTime convertToDateTime(String afterDateTime) {
        LocalDateTime localDateTime = null;
        if (afterDateTime == null || afterDateTime.length() == 0) {
            localDateTime = DateMassager.getLocalDateTime();
        } else {
            localDateTime = DateMassager.createCustomDateTimeFromHrAndMin(afterDateTime);
        }
        return localDateTime;
    }

    public static int convertToTimeToSeconds(String timeAsText) {
        String[] hms = timeAsText.split(":");
        int hr = Integer.valueOf(hms.length >= 1 ? hms[0] : "0");
        int minute = Integer.valueOf(hms.length >= 2 ? hms[1] : "0");
        int seconds = Integer.valueOf(hms.length >= 3 ? hms[2] : "0");
        minute += hr * 60;
        seconds += minute * 60;
        return seconds;
    }

}