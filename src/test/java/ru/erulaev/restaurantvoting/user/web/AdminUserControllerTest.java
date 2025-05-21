package ru.erulaev.restaurantvoting.user.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.user.UserTestData;
import ru.erulaev.restaurantvoting.user.model.User;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.user.UserTestData.*;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.web.AdminUserController.REST_URL;

public class AdminUserControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonMatcher(user, UserTestData::assertEquals));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        List<User> actualUsers = asList(perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn());
        assertEquals(actualUsers, admin, guest, user);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-email?email=" + ADMIN_MAIL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonMatcher(admin, UserTestData::assertEquals));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_ID))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.findById(USER_ID).isPresent());
        Assertions.assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        User newUser = UserTestData.getNew();
        User created = asUser(perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newUser)))
                .andExpect(status().isCreated()).andReturn());
        int newId = created.id();
        newUser.setId(newId);
        UserTestData.assertEquals(created, newUser);
        UserTestData.assertEquals(created, userRepository.findById(newId).orElseThrow());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        User updated = UserTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());
        UserTestData.assertEquals(updated, userRepository.findById(USER_ID).orElseThrow());
    }
}
