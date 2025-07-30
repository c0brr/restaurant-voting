package ru.erulaev.restaurantvoting.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;

public class AbstractRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    protected RestaurantRepository restaurantRepository;
}
