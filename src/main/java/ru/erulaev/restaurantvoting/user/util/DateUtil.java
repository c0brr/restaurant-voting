package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.common.error.DataConflictException;

import java.time.LocalDate;

@UtilityClass
public class DateUtil {

    public static final LocalDate APPLICATION_START = LocalDate.of(2025, 1, 1);

    public static void processDate(LocalDate date) {
        LocalDate currenDate = LocalDate.now();
        date = date != null ? date : currenDate;
        if (date.isBefore(APPLICATION_START) || date.isAfter(currenDate)) {
            throw new DataConflictException("No data for this date");
        }
    }
}
