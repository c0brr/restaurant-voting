package ru.erulaev.restaurantvoting.user.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.entity.Menu;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.to.menu.AdminMenuTo;
import ru.erulaev.restaurantvoting.user.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.validation.UniqueDateMenuValidator.EXCEPTION_DUPLICATE_MENU;
import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.ADMIN_MAIL;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String RESTAURANT_1_REST_URL = "/api/admin/restaurants/1/menus";
    private static final String RESTAURANT_2_REST_URL = "/api/admin/restaurants/2/menus";
    private static final String RESTAURANT_4_REST_URL = "/api/admin/restaurants/4/menus";
    private static final String RESTAURANT_NOT_EXIST_REST_URL = "/api/admin/restaurants/100/menus";
    private static final String RESTAURANT_1_REST_URL_SLASH = RESTAURANT_1_REST_URL + '/';
    private static final String RESTAURANT_2_REST_URL_SLASH = RESTAURANT_2_REST_URL + '/';
    private static final String RESTAURANT_NOT_EXIST_REST_URL_SLASH = RESTAURANT_NOT_EXIST_REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ADMIN_MENU_TO_MATCHER.contentJson(adminMenuTo5, adminMenuTo4, adminMenuTo3, adminMenuTo2, adminMenuTo1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_NOT_EXIST_REST_URL))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForRestaurantWithEmptyMenus() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_4_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ADMIN_MENU_TO_MATCHER.contentJson());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_2_REST_URL_SLASH + MENU_6_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ADMIN_MENU_TO_MATCHER.contentJson(adminMenuTo6));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getForWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL_SLASH + MENU_6_ID))
                .andDo(print())
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
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_1_REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = new Menu();
        ResultActions action = perform(MockMvcRequestBuilders.post(RESTAURANT_1_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMenu)))
                .andExpect(status().isCreated());

        AdminMenuTo created = ADMIN_MENU_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        newMenu.setParentEntity(restaurantRepository.getReferenceById(created.getRestaurantId()));
        AdminMenuTo newMenuTo = getAdminTo(newMenu);
        ADMIN_MENU_TO_MATCHER.assertMatch(created, newMenuTo);
        created = getAdminTo(menuRepository.findById(newId).orElseThrow(() ->
                new NotFoundException("Entity with id=" + newId + " not found")));
        ADMIN_MENU_TO_MATCHER.assertMatch(created, newMenuTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post(RESTAURANT_NOT_EXIST_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new Menu())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Menu expected = new Menu(null, adminMenuTo6.getCreationDate());
        perform(MockMvcRequestBuilders.post(RESTAURANT_2_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_MENU)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT_1_REST_URL_SLASH + MENU_5_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findById(MENU_5_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT_1_REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteForWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT_1_REST_URL_SLASH + MENU_6_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteForNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT_NOT_EXIST_REST_URL_SLASH + MENU_1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}