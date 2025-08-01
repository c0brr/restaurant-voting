package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.MenuMapper;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.to.menu.AdminMenuTo;

import java.time.LocalDate;

import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.restaurant1;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.restaurant2;

public class MenuTestData {

    public static final MatcherFactory.Matcher<AdminMenuTo> ADMIN_MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(AdminMenuTo.class);
    public static final MenuMapper MAPPER = Mappers.getMapper(MenuMapper.class);

    public static final long MENU_1_ID = 1L;
    public static final long MENU_2_ID = 2L;
    public static final long MENU_3_ID = 3L;
    public static final long MENU_4_ID = 4L;
    public static final long MENU_5_ID = 5L;
    public static final long MENU_6_ID = 6L;
    public static final long NOT_FOUND = 100L;

    public static final Menu menu1 = new Menu(MENU_1_ID, LocalDate.of(2024, 5, 6), restaurant1);
    public static final Menu menu2 = new Menu(MENU_2_ID, LocalDate.of(2024, 6, 6), restaurant1);

    public static final AdminMenuTo menuTo1 = MAPPER.createTo(new Menu(MENU_1_ID, LocalDate.of(2024, 5, 6), restaurant1));
    public static final AdminMenuTo menuTo2 = MAPPER.createTo(new Menu(MENU_2_ID, LocalDate.of(2024, 6, 6), restaurant1));
    public static final AdminMenuTo menuTo3 = MAPPER.createTo(new Menu(MENU_3_ID, LocalDate.of(2024, 6, 7), restaurant1));
    public static final AdminMenuTo menuTo4 = MAPPER.createTo(new Menu(MENU_4_ID, LocalDate.of(2024, 6, 8), restaurant1));
    public static final AdminMenuTo menuTo5 = MAPPER.createTo(new Menu(MENU_5_ID, LocalDate.of(2025, 1, 1), restaurant1));
    public static final AdminMenuTo menuTo6 = MAPPER.createTo(new Menu(MENU_6_ID, LocalDate.of(2024, 1, 1), restaurant2));

    public static AdminMenuTo getTo(Menu menu) {
        return MAPPER.createTo(menu);
    }
}
