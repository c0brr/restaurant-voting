package ru.erulaev.restaurantvoting.user.util;

import lombok.experimental.UtilityClass;
import ru.erulaev.restaurantvoting.user.model.*;
import ru.erulaev.restaurantvoting.user.to.*;

@UtilityClass
public class ToConverter {

    public static DishTo createTo(Dish dish, long menuId) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), menuId);
    }

    public static MenuTo createTo(Menu menu, long restaurantId) {
        return new MenuTo(menu.getId(), menu.getCreated(), restaurantId);
    }

    public static RestaurantTo createTo(Restaurant restaurant, int votes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getRegistered(), votes);
    }

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static Vote createNewFromRequestTo(RequestVoteTo requestVoteTo, User user, Restaurant restaurant) {
        return new Vote(null, requestVoteTo.getCreated(), user, restaurant);
    }

    public static ResponseVoteTo createResponseTo(Vote vote) {
        return new ResponseVoteTo(vote.getId(), vote.getCreated(), vote.getUserId(), vote.getRestaurantId());
    }
}
