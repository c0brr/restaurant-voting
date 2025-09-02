package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.DishMapper;
import ru.erulaev.restaurantvoting.user.entity.Dish;
import ru.erulaev.restaurantvoting.user.to.dish.DishTo;
import ru.erulaev.restaurantvoting.user.to.dish.DishWithMenuIdTo;

import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.menu1;
import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.menu2;

public class DishTestData {

    public static final MatcherFactory.Matcher<DishWithMenuIdTo> DISH_WITH_MENU_ID_TO_MATCHER =
            MatcherFactory.usingEqualsComparator(DishWithMenuIdTo.class);
    public static final DishMapper MAPPER = Mappers.getMapper(DishMapper.class);

    public static final int DISH_1_ID = 1;
    public static final int DISH_2_ID = 2;
    public static final int DISH_3_ID = 3;
    public static final int DISH_4_ID = 4;
    public static final int DISH_5_ID = 5;
    public static final int NOT_FOUND = 100;
    public static final String DISH_1_NAME = "Soup";
    public static final String DISH_2_NAME = "Salad";
    public static final String DISH_3_NAME = "Fries";
    public static final String DISH_4_NAME = "Apple juice";
    public static final String DISH_5_NAME = "Salad";

    public static final Dish dish1 = new Dish(DISH_1_ID, DISH_1_NAME, 260, menu1);
    public static final Dish dish2 = new Dish(DISH_2_ID, DISH_2_NAME, 200, menu1);
    public static final Dish dish3 = new Dish(DISH_3_ID, DISH_3_NAME, 100, menu1);
    public static final Dish dish4 = new Dish(DISH_4_ID, DISH_4_NAME, 150, menu1);
    public static final Dish dish5 = new Dish(DISH_5_ID, DISH_5_NAME, 50, menu2);

    public static final DishWithMenuIdTo dishWithMenuIdTo1 = MAPPER.createWithMenuIdTo(dish1);
    public static final DishWithMenuIdTo dishWithMenuIdTo2 = MAPPER.createWithMenuIdTo(dish2);
    public static final DishWithMenuIdTo dishWithMenuIdTo3 = MAPPER.createWithMenuIdTo(dish3);
    public static final DishWithMenuIdTo dishWithMenuIdTo4 = MAPPER.createWithMenuIdTo(dish4);
    public static final DishWithMenuIdTo dishWithMenuIdTo5 = MAPPER.createWithMenuIdTo(dish5);

    public static final DishTo dishTo1 = MAPPER.createTo(dish1);
    public static final DishTo dishTo2 = MAPPER.createTo(dish2);
    public static final DishTo dishTo3 = MAPPER.createTo(dish3);
    public static final DishTo dishTo4 = MAPPER.createTo(dish4);
    public static final DishTo dishTo5 = MAPPER.createTo(dish5);

    public static Dish getNew() {
        return new Dish(null, "NewDish", 200);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_5_ID, "UpdatedName", 888);
    }

    public static DishWithMenuIdTo getWithMenuIdTo(Dish dish) {
        return MAPPER.createWithMenuIdTo(dish);
    }
}
