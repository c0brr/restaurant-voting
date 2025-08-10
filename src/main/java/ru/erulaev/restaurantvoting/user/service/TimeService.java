package ru.erulaev.restaurantvoting.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.erulaev.restaurantvoting.common.error.DataConflictException;

import java.time.Clock;
import java.time.LocalTime;

@Service
public class TimeService {

    @Value("${app.voting-deadline}")
    private LocalTime votingDeadline;

    public LocalTime getCurrentTime() {
        return LocalTime.now(Clock.systemUTC());
    }

    public boolean isDeadLinePassed() {
        return getCurrentTime().isAfter(votingDeadline);
    }

    public void checkDeadLine() {
        if (isDeadLinePassed()) {
            throw new DataConflictException("Voting is over for today");
        }
    }
}
