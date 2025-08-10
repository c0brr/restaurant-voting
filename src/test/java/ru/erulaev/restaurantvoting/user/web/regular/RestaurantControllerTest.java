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
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;
import static ru.erulaev.restaurantvoting.user.web.regular.RestaurantController.REST_URL;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @MockitoSpyBean
    private DateService dateService;

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAllByDate() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1CurrentDate, restaurantTo4CurrentDate,
                        restaurantTo2CurrentDate, restaurantTo3CurrentDate));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAllByOtherDate() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", dateService.getDateAsString(OTHER_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1OtherDate, restaurantTo4OtherDate,
                        restaurantTo2OtherDate, restaurantTo3OtherDate));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAllByNotValidDate1() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", dateService.getDateAsString(NOT_VALID_DATE1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAllByNotValidDate2() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", dateService.getDateAsString(NOT_VALID_DATE2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void badDateFormat() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", "bad format"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void get() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1CurrentDate));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getNotFound() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getByName() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "/by-name?name=" + RESTAURANT_1_NAME.toLowerCase()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1CurrentDate));
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getByNameNotFound() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(CURRENT_DATE);
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "/by-name?name=" + NOT_FOUND_NAME))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}