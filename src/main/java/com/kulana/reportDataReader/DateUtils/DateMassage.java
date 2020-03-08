package com.kulana.reportDataReader.DateUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateMassage {
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
        return DateMassage.convertTextToLocalDateTime(dateTime);
    }

    public static LocalDateTime createCustomDateTimeFromHrAndMin(String afterDateTime) {
        String[] arrs = afterDateTime.split(":");
        LocalDateTime localDateTime = DateMassage.getLocalDateTime();

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
            localDateTime = DateMassage.getLocalDateTime();
        } else {
            localDateTime = DateMassage.createCustomDateTimeFromHrAndMin(afterDateTime);
        }
        return localDateTime;
    }

    public static int convertTimeToSeconds(String timeAsText) {
        String[] hms = timeAsText.split(":");
        int hr = Integer.valueOf(hms.length >= 1 ? hms[0] : "0");
        int minute = Integer.valueOf(hms.length >= 2 ? hms[1] : "0");
        int seconds = Integer.valueOf(hms.length >= 3 ? hms[2] : "0");
        minute += hr * 60;
        seconds += minute * 60;
        return seconds;
    }
}