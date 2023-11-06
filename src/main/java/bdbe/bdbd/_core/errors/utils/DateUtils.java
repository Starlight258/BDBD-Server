package bdbe.bdbd._core.errors.utils;

import bdbe.bdbd.optime.DayType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static DayType getDayType(DayOfWeek dayOfWeek) {
        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return DayType.WEEKEND;
        } else {
            return DayType.WEEKDAY;
        }
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatTime(LocalTime time) {
        return time.format(timeFormatter);
    }
}
