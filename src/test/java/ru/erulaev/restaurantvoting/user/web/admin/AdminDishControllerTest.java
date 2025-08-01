package ru.erulaev.restaurantvoting.user.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.repository.DishRepository;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.to.DishTo;
import ru.erulaev.restaurantvoting.user.util.NameUtil;
import ru.erulaev.restaurantvoting.user.web.AbstractControllerTest;
import ru.erulaev.restaurantvoting.user.web.data.DishTestData;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.validation.UniqueDishNameValidator.EXCEPTION_DUPLICATE_NAME;
import static ru.erulaev.restaurantvoting.user.web.data.DishTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.DishTestData.getTo;
import static ru.erulaev.restaurantvoting.user.web.data.MenuTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.RESTAURANT_1_ID;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.ADMIN_MAIL;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;


class AdminDishControllerTest extends AbstractControllerTest {

    private static final String MENU_1_REST_URL = "/api/admin/menus/1/dishes";
    private static final String MENU_2_REST_URL = "/api/admin/menus/2/dishes";
    private static final String MENU_4_REST_URL = "/api/admin/menus/4/dishes";
    private static final String MENU_NOT_EXIST_REST_URL = "/api/admin/menus/100/dishes";
    private static final String MENU_1_REST_URL_SLASH = MENU_1_REST_URL + '/';
    private static final String MENU_2_REST_URL_SLASH = MENU_2_REST_URL + '/';
    private static final String MENU_NOT_EXIST_REST_URL_SLASH = MENU_NOT_EXIST_REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(dishTo4, dishTo3, dishTo2, dishTo1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForNotExistMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_NOT_EXIST_REST_URL))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForMenuWithEmptyDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_4_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_2_REST_URL_SLASH + DISH_5_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(dishTo5));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL_SLASH + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getForWrongMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL_SLASH + DISH_5_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMenuAndGet() throws Exception {
        menuRepository.delete(MENU_1_ID, RESTAURANT_1_ID);
        perform(MockMvcRequestBuilders.get(MENU_1_REST_URL_SLASH + MENU_4_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(MENU_1_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andExpect(status().isCreated());

        DishTo created = DISH_TO_MATCHER.readFromJson(action);
        long newId = created.id();
        newDish.setId(newId);
        newDish.setParentEntity(menuRepository.getReferenceById(created.getMenuId()));
        newDish.setName(NameUtil.getCorrectName(newDish.getName()));
        DishTo newDishTo = getTo(newDish);
        DISH_TO_MATCHER.assertMatch(created, newDishTo);
        created = getTo(dishRepository.findById(newId).orElseThrow(() ->
                new NotFoundException("Entity with id=" + newId + " not found")));
        DISH_TO_MATCHER.assertMatch(created, newDishTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForNotExistMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(MENU_NOT_EXIST_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new Dish("NewDish", 100))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "", 555555555);
        perform(MockMvcRequestBuilders.post(MENU_1_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Dish expected = new Dish(null, DISH_2_NAME.toLowerCase(), 111);
        perform(MockMvcRequestBuilders.post(MENU_1_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(MENU_1_REST_URL_SLASH + DISH_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(DISH_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(MENU_1_REST_URL_SLASH + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteForWrongMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(MENU_1_REST_URL_SLASH + DISH_5_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteForNotExistMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(MENU_NOT_EXIST_REST_URL_SLASH + MENU_1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(MENU_2_REST_URL_SLASH + DISH_5_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        updated = DishTestData.getUpdated();
        updated.setName(NameUtil.getCorrectName(updated.getName()));
        updated.setParentEntity(menuRepository.getReferenceById(MENU_2_ID));
        DishTo updatedTo = getTo(updated);
        DISH_TO_MATCHER.assertMatch(getTo(dishRepository.findById(DISH_5_ID).orElseThrow(() ->
                new NotFoundException("Entity with id=" + DISH_5_ID + " not found"))), updatedTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Dish updated = new Dish(dish1);
        updated.setName(DISH_2_NAME);
        perform(MockMvcRequestBuilders.put(MENU_1_REST_URL_SLASH + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(dish1);
        invalid.setName("");
        invalid.setPrice(22222222);
        perform(MockMvcRequestBuilders.put(MENU_1_REST_URL_SLASH + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}