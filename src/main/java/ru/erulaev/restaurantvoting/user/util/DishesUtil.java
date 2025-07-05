package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.to.DishTo;
import ru.erulaev.restaurantvoting.user.to.MenuTo;

@UtilityClass
public class DishesUtil {

    public static DishTo createTo(Dish dish, long menuId) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), menuId);
    }
}
