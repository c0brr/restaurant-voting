package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.RestaurantMapper;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.to.RestaurantTo;

import java.time.Instant;
import java.time.LocalDate;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "created");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "created");
    public static final RestaurantMapper MAPPER = Mappers.getMapper(RestaurantMapper.class);

    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final int RESTAURANT_3_ID = 3;
    public static final int RESTAURANT_4_ID = 4;
    public static final int NOT_FOUND = 100;
    public static final String RESTAURANT_1_NAME = "First_restaurant";
    public static final String RESTAURANT_2_NAME = "Second_restaurant";
    public static final String RESTAURANT_3_NAME = "Third_restaurant";
    public static final String RESTAURANT_4_NAME = "Fourth_restaurant";
    public static final String NOT_FOUND_NAME = "Not found";

    public static final LocalDate CURRENT_DATE = LocalDate.of(2025, 7, 30);
    public static final LocalDate OTHER_DATE = LocalDate.of(2025, 7, 7);
    public static final LocalDate NOT_VALID_DATE_FUTURE = LocalDate.of(3000, 1, 1);
    public static final LocalDate NOT_VALID_DATE_PAST = LocalDate.of(2000, 1, 1);

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, RESTAURANT_1_NAME);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, RESTAURANT_2_NAME);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, RESTAURANT_3_NAME);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT_4_ID, RESTAURANT_4_NAME);
    public static final RestaurantTo restaurantTo1CurrentDate = getTo(restaurant1, 0);
    public static final RestaurantTo restaurantTo2CurrentDate = getTo(restaurant2, 0);
    public static final RestaurantTo restaurantTo3CurrentDate = getTo(restaurant3, 1);
    public static final RestaurantTo restaurantTo4CurrentDate = getTo(restaurant4, 0);
    public static final RestaurantTo restaurantTo1OtherDate = getTo(restaurant1, 2);
    public static final RestaurantTo restaurantTo2OtherDate = getTo(restaurant2, 1);
    public static final RestaurantTo restaurantTo3OtherDate = getTo(restaurant3, 0);
    public static final RestaurantTo restaurantTo4OtherDate = getTo(restaurant4, 0);

    public static Restaurant getNew() {
        return new Restaurant(null, "New", Instant.now());
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "UpdatedName", Instant.now());
    }

    public static RestaurantTo getTo(Restaurant restaurant, int votes) {
        return MAPPER.createTo(restaurant, votes);
    }
}
