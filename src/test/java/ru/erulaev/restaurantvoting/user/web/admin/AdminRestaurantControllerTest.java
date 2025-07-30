package ru.erulaev.restaurantvoting.user.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.web.AbstractRestaurantControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.validation.UniqueRestaurantNameValidator.EXCEPTION_DUPLICATE_NAME;
import static ru.erulaev.restaurantvoting.user.web.admin.AdminRestaurantController.REST_URL;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.ADMIN_MAIL;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;

class AdminRestaurantControllerTest extends AbstractRestaurantControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant4, restaurant2, restaurant3));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_2_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
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
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        long newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(newId).orElseThrow(() ->
                new NotFoundException("Entity with id=" + newId + " not found")), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Restaurant expected = new Restaurant(null, RESTAURANT_1_NAME);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT_3_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RESTAURANT_3_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(RESTAURANT_1_ID).orElseThrow(() ->
                new NotFoundException("Entity with id=" + RESTAURANT_1_ID + " not found")), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName(RESTAURANT_2_NAME);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(restaurant1);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-name?name=" + restaurant4.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant4));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByContainingName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-containing-name?name=f"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant4));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getEmptyListByContainingName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-containing-name?name=fifth"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson());
    }
}