package ru.erulaev.restaurantvoting.user.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.user.UserTestData;
import ru.erulaev.restaurantvoting.user.model.User;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.user.UserTestData.*;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.web.ProfileController.REST_URL;

public class ProfileControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonMatcher(user, UserTestData::assertEquals));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.findById(USER_ID).isPresent());
        Assertions.assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    void register() throws Exception {
        User newUser = UserTestData.getNew();
        User registered = asUser(perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newUser)))
                .andExpect(status().isCreated()).andReturn());
        int newId = registered.id();
        newUser.setId(newId);
        UserTestData.assertEquals(registered, newUser);
        UserTestData.assertEquals(registered, userRepository.findById(newId).orElseThrow());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        User updated = UserTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        UserTestData.assertEquals(updated, userRepository.findById(USER_ID).orElseThrow());
    }
}
