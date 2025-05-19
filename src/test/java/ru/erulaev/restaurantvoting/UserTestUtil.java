package ru.erulaev.restaurantvoting;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.erulaev.restaurantvoting.model.Role;
import ru.erulaev.restaurantvoting.model.User;
import ru.erulaev.restaurantvoting.util.JsonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtil {

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int GUEST_ID = 3;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@yandex.ru";
    public static final String GUEST_MAIL = "guest@gmail.com";
    public static final User user = new User(USER_ID, "User_First", USER_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin_First", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest_First", GUEST_MAIL, "guest");

    public static User getNew() {
        return new User(null, "New_First", "new@gmail.com", "newpass", Role.USER);
    }

    public static User getUpdated() {
        return new User(USER_ID, "User_First_Update", "user_update@gmail.com", "password_update", Role.USER);
    }

    public static void assertEquals(User actual, User expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("password", "registered").isEqualTo(expected);
    }

    public static void assertEquals(Iterable<User> actual, User... expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("password", "registered")
                .isEqualTo(List.of(expected));
    }

    public static User asUser(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, User.class);
    }

    public static List<User> asList(MvcResult mvcResult) throws IOException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValues(jsonActual, User.class);
    }

    public static ResultMatcher jsonMatcher(User expected, BiConsumer<User, User> equalsAssertion) {
        return mvcResult -> equalsAssertion.accept(asUser(mvcResult), expected);
    }
}