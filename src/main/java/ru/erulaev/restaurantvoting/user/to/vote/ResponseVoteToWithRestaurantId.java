package ru.erulaev.restaurantvoting.user.to.vote;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ResponseVoteToWithRestaurantId extends BaseTo {

    private LocalDate date;

    private int restaurantId;

    private int userId;

    @Override
    public String toString() {
        return "ResponseVoteToWithRestaurantId:" + id;
    }
}