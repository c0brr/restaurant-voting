package ru.erulaev.restaurantvoting.user;

import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.common.util.JsonUtil;
import ru.erulaev.restaurantvoting.user.model.Role;
import ru.erulaev.restaurantvoting.user.model.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class UserTestData {

    public static final MatcherFactory.Matcher<User> USER_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final long USER_1_ID = 1L;
    public static final long USER_2_ID = 2L;
    public static final long USER_3_ID = 3L;
    public static final long ADMIN_ID = 4L;
    public static final long GUEST_ID = 5L;
    public static final long NOT_FOUND = 100L;
    public static final String USER_1_MAIL = "user@gmail.com";
    public static final String USER_2_MAIL = "user2@gmail.com";
    public static final String USER_3_MAIL = "user3@yandex.ru";
    public static final String ADMIN_MAIL = "admin@yandex.ru";
    public static final String GUEST_MAIL = "guest@gmail.com";

    public static final User user1 = new User(USER_1_ID, "First_User", USER_1_MAIL, "password", Role.USER);
    public static final User user2 = new User(USER_2_ID, "Second_User", USER_2_MAIL, "password2", Role.USER);
    public static final User user3 = new User(USER_3_ID, "Third_User", USER_3_MAIL, "password3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "First_Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User guest = new User(GUEST_ID, "First_Guest", GUEST_MAIL, "guest");

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, Instant.now(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_1_ID, "UpdatedName", USER_1_MAIL, "newPass", false, Instant.now(), List.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}