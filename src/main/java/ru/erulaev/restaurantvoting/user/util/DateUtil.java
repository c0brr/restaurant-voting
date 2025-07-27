package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;

import java.time.LocalDate;

@UtilityClass
public class DateUtil {

    public static LocalDate processDate(LocalDate date) {
        LocalDate currenDate = LocalDate.now();
        date = date != null ? date : currenDate;
        if (date.isAfter(currenDate)) {
            throw new NotFoundException("No available data for this date");
        }
        return date;
    }
}
