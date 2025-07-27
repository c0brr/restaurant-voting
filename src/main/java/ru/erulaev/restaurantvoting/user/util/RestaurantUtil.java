package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.time.LocalDate;
import java.time.ZoneId;

@UtilityClass
public class RestaurantUtil {

    public static boolean isRestaurantExistedByDate(Restaurant restaurant, LocalDate date) {
        return !restaurant.getRegistered().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(date);
    }
}
