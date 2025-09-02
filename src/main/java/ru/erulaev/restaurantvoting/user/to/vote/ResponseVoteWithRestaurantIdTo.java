package ru.erulaev.restaurantvoting.user.to.vote;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ResponseVoteWithRestaurantIdTo extends BaseTo {

    private LocalDate creationDate;

    private int restaurantId;

    private int userId;

    @Override
    public String toString() {
        return "ResponseVoteWithRestaurantIdTo:" + id;
    }
}