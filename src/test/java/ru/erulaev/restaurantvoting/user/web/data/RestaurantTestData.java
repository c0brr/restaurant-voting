package ru.erulaev.restaurantvoting.user.web.data;

import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.time.Instant;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "created");

    public static final long RESTAURANT_1_ID = 1L;
    public static final long RESTAURANT_2_ID = 2L;
    public static final long RESTAURANT_3_ID = 3L;
    public static final long RESTAURANT_4_ID = 4L;
    public static final long NOT_FOUND = 100L;
    public static final String RESTAURANT_1_NAME = "First_restaurant";
    public static final String RESTAURANT_2_NAME = "Second_restaurant";
    public static final String RESTAURANT_3_NAME = "Third_restaurant";
    public static final String RESTAURANT_4_NAME = "Fourth_restaurant";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, RESTAURANT_1_NAME);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, RESTAURANT_2_NAME);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, RESTAURANT_3_NAME);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT_4_ID, RESTAURANT_4_NAME);

    public static Restaurant getNew() {
        return new Restaurant(null, "New", Instant.now());
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "UpdatedName", Instant.now());
    }
}
