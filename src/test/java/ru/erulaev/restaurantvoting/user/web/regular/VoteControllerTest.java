package ru.erulaev.restaurantvoting.user.web.regular;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.user.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.GUEST_MAIL;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.USER_1_MAIL;
import static ru.erulaev.restaurantvoting.user.web.data.VoteTestData.*;
import static ru.erulaev.restaurantvoting.user.web.regular.VoteController.REST_URL;

class VoteControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESPONSE_VOTE_TO_MATCHER
                        .contentJson(responseVoteTo4, responseVoteTo3, responseVoteTo2, responseVoteTo1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }
}