package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NameUtil {

    public static String getCorrectName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
    }
}
