package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.to.MenuTo;

@UtilityClass
public class MenusUtil {

    public static MenuTo createTo(Menu menu, long restaurantId) {
        return new MenuTo(menu.getId(), menu.getDate(), restaurantId);
    }
}
