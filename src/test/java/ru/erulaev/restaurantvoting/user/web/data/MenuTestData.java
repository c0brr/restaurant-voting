package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.MenuMapper;
import ru.erulaev.restaurantvoting.user.entity.Menu;
import ru.erulaev.restaurantvoting.user.to.dish.DishTo;
import ru.erulaev.restaurantvoting.user.to.menu.AdminMenuTo;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;

import java.time.LocalDate;
import java.util.Arrays;

import static ru.erulaev.restaurantvoting.user.web.data.DishTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.restaurant1;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.restaurant2;

public class MenuTestData {

    public static final MatcherFactory.Matcher<AdminMenuTo> ADMIN_MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(AdminMenuTo.class);
    public static final MatcherFactory.Matcher<RegularMenuTo> REGULAR_MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(RegularMenuTo.class);
    public static final MenuMapper MAPPER = Mappers.getMapper(MenuMapper.class);

    public static final int MENU_1_ID = 1;
    public static final int MENU_2_ID = 2;
    public static final int MENU_3_ID = 3;
    public static final int MENU_4_ID = 4;
    public static final int MENU_5_ID = 5;
    public static final int MENU_6_ID = 6;
    public static final int NOT_FOUND = 100;

    public static final LocalDate DATE_NOT_FOUND = LocalDate.of(2024, 1, 1);

    public static final Menu menu1 = new Menu(MENU_1_ID, LocalDate.of(2024, 5, 6), restaurant1);
    public static final Menu menu2 = new Menu(MENU_2_ID, LocalDate.of(2024, 6, 6), restaurant1);

    public static final AdminMenuTo adminMenuTo1 = getAdminTo(menu1);
    public static final AdminMenuTo adminMenuTo2 = getAdminTo(menu2);
    public static final AdminMenuTo adminMenuTo3 = getAdminTo(new Menu(MENU_3_ID, LocalDate.of(2024, 6, 7), restaurant1));
    public static final AdminMenuTo adminMenuTo4 = getAdminTo(new Menu(MENU_4_ID, LocalDate.of(2024, 6, 8), restaurant1));
    public static final AdminMenuTo adminMenuTo5 = getAdminTo(new Menu(MENU_5_ID, LocalDate.of(2025, 1, 1), restaurant1));
    public static final AdminMenuTo adminMenuTo6 = getAdminTo(new Menu(MENU_6_ID, LocalDate.of(2024, 1, 1), restaurant2));

    public static final RegularMenuTo regularMenuTo1 = getRegularTo(menu1, dishTo4, dishTo3, dishTo2, dishTo1);
    public static final RegularMenuTo regularMenuTo2 = getRegularTo(menu2, dishTo5);

    public static AdminMenuTo getAdminTo(Menu menu) {
        return MAPPER.createTo(menu);
    }

    public static RegularMenuTo getRegularTo(Menu menu, DishTo... dishTos) {
        RegularMenuTo regularMenuTo = MAPPER.createWithDishesTo(menu);
        regularMenuTo.setDishTos(Arrays.asList(dishTos));
        return regularMenuTo;
    }
}
