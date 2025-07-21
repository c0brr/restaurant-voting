package ru.erulaev.restaurantvoting.user.to.vote;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ResponseVoteTo extends BaseTo {

    private LocalDate date;

    private long restaurantId;

    private long userId;

    @Override
    public String toString() {
        return "ResponseVoteTo:" + id;
    }
}