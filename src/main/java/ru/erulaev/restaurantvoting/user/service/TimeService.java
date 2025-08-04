package ru.erulaev.restaurantvoting.user.service;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;

@Service
public class TimeService {

    public LocalTime getCurrentTime() {
        return LocalTime.now(Clock.systemUTC());
    }

    public boolean isDeadLinePassed(LocalTime time) {
        return getCurrentTime().isAfter(time);
    }
}
