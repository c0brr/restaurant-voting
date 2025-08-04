package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.time.LocalDate;
import java.time.ZoneId;

@UtilityClass
public class RestaurantUtil {

    public static boolean isRestaurantExistedAtDate(Restaurant restaurant, LocalDate date) {
        return !restaurant.getCreated().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(date);
    }
}
