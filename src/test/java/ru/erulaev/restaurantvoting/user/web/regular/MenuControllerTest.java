package ru.erulaev.restaurantvoting.user.web.regular;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.user.service.DateService;
import ru.erulaev.restaurantvoting.user.web.AbstractControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;

class MenuControllerTest extends AbstractControllerTest {

    private static final String RESTAURANT_1_REST_URL = "/api/restaurants/1/menu";
    private static final String RESTAURANT_NOT_EXIST_URL = "/api/restaurants/100/menu";

    @MockitoSpyBean
    private DateService dateService;

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getByDate() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(regularMenuTo1.getCreationDate());
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REGULAR_MENU_TO_MATCHER.contentJson(regularMenuTo1));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getByOtherDate() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(regularMenuTo2.getCreationDate());
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL)
                .param("date", dateService.getDateAsString(regularMenuTo1.getCreationDate())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REGULAR_MENU_TO_MATCHER.contentJson(regularMenuTo1));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getByNotFoundDate() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(regularMenuTo1.getCreationDate());
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL)
                .param("date", dateService.getDateAsString(DATE_NOT_FOUND)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getForNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_NOT_EXIST_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void badDateFormat() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL)
                .param("date", "bad format"))
                .andExpect(status().isBadRequest());
    }
}