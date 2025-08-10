package ru.erulaev.restaurantvoting.user.service;

import org.springframework.stereotype.Service;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class DateService {

    public LocalDate getCurrentDate() {
        return LocalDate.now(Clock.systemUTC());
    }

    public LocalDate processDate(LocalDate date) {
        LocalDate currenDate = getCurrentDate();
        date = date != null ? date : currenDate;
        if (date.isAfter(currenDate)) {
            throw new NotFoundException("No available data for this date");
        }
        return date;
    }

    public String getDateAsString(LocalDate date) {
        return date.toString();
    }
}
