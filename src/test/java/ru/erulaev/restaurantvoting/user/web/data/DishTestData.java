package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.DishMapper;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.menu1;
import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.menu2;

public class DishTestData {

    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);
    public static final DishMapper MAPPER = Mappers.getMapper(DishMapper.class);

    public static final long DISH_1_ID = 1L;
    public static final long DISH_2_ID = 2L;
    public static final long DISH_3_ID = 3L;
    public static final long DISH_4_ID = 4L;
    public static final long DISH_5_ID = 5L;
    public static final long NOT_FOUND = 100L;
    public static final String DISH_1_NAME = "Soup";
    public static final String DISH_2_NAME = "Salad";
    public static final String DISH_3_NAME = "Fries";
    public static final String DISH_4_NAME = "Apple juice";
    public static final String DISH_5_NAME = "Salad";

    public static final Dish dish1 = new Dish(DISH_1_ID, DISH_1_NAME, 260, menu1);

    public static final DishTo dishTo1 = MAPPER.createTo(dish1);
    public static final DishTo dishTo2 = MAPPER.createTo(new Dish(DISH_2_ID, DISH_2_NAME, 200, menu1));
    public static final DishTo dishTo3 = MAPPER.createTo(new Dish(DISH_3_ID, DISH_3_NAME, 100, menu1));
    public static final DishTo dishTo4 = MAPPER.createTo(new Dish(DISH_4_ID, DISH_4_NAME, 150, menu1));
    public static final DishTo dishTo5 = MAPPER.createTo(new Dish(DISH_5_ID, DISH_5_NAME, 50, menu2));

    public static Dish getNew() {
        return new Dish(null, "NewDish", 200);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_5_ID, "UpdatedName", 888);
    }

    public static DishTo getTo(Dish dish) {
        return MAPPER.createTo(dish);
    }
}
