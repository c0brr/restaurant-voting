package ru.erulaev.restaurantvoting.user.web.regular;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.util.JsonUtil;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.service.DateService;
import ru.erulaev.restaurantvoting.user.service.TimeService;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;
import ru.erulaev.restaurantvoting.user.web.AbstractControllerTest;
import ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.erulaev.restaurantvoting.common.util.JsonUtil.writeValue;
import static ru.erulaev.restaurantvoting.user.service.VoteService.VOTING_DEADLINE;
import static ru.erulaev.restaurantvoting.user.validation.UniqueUserVoteValidator.EXCEPTION_DUPLICATE_VOTE;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.RESTAURANT_2_ID;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.RESTAURANT_4_ID;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.VoteTestData.*;
import static ru.erulaev.restaurantvoting.user.web.regular.CurrentVoteController.REST_URL;

class CurrentVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @MockitoSpyBean
    private TimeService timeService;

    @MockitoSpyBean
    private DateService dateService;

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void get() throws Exception {
        when(dateService.getCurrentDate()).thenReturn(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESPONSE_VOTE_TO_MATCHER.contentJson(responseVoteTo5));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void createWithLocation() throws Exception {
        processMockServices(CORRECT_DATE);
        RequestVoteTo newRequestTo = new RequestVoteTo(RESTAURANT_4_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRequestTo)))
                .andExpect(status().isCreated());

        ResponseVoteTo createdResponseTo = RESPONSE_VOTE_TO_MATCHER.readFromJson(action);
        long newId = createdResponseTo.id();
        newRequestTo.setId(newId);

        matchVotes(createdResponseTo, newRequestTo, USER_2_ID);
        createdResponseTo = getResponseTo(voteRepository.findById(newId).orElseThrow(() ->
                new NotFoundException("Entity with id=" + newId + " not found")));
        matchVotes(createdResponseTo, newRequestTo, USER_2_ID);
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void createDuplicate() throws Exception {
        processMockServices(responseVoteTo5.getDate());
        RequestVoteTo expected = new RequestVoteTo(RESTAURANT_4_ID, responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_VOTE)));
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void createInvalid() throws Exception {
        processMockServices(CORRECT_DATE);
        RequestVoteTo newRequestTo = new RequestVoteTo(-1);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRequestTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForNotExistRestaurant() throws Exception {
        processMockServices(CORRECT_DATE);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new RequestVoteTo(RestaurantTestData.NOT_FOUND))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void createWhenDeadLinePassed() throws Exception {
        processMockServicesWhenDeadlinePassed(CORRECT_DATE);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(new RequestVoteTo(RESTAURANT_4_ID))))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void delete() throws Exception {
        processMockServices(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        assertFalse(voteRepository.findById(VOTE_5_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void deleteNotFound() throws Exception {
        processMockServices(DATE_NOT_FOUND);
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void deleteWhenDeadLinePassed() throws Exception {
        processMockServicesWhenDeadlinePassed(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void changeChoice() throws Exception {
        processMockServices(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.patch(REST_URL)
                .param("restaurantId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Long voteId = responseVoteTo5.getId();
        assertEquals(RESTAURANT_2_ID, voteRepository.findById(voteId).orElseThrow(() ->
                new NotFoundException("Entity with id=" + voteId + " not found")).getRestaurantId());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void changeChoiceForNotFoundRestaurant() throws Exception {
        processMockServices(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.patch(REST_URL)
                .param("restaurantId", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void changeChoiceWhenDeadLinePassed() throws Exception {
        processMockServicesWhenDeadlinePassed(responseVoteTo5.getDate());
        perform(MockMvcRequestBuilders.patch(REST_URL)
                .param("restaurantId", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    private void processMockServices(LocalDate returningDate) {
        when(timeService.isDeadLinePassed(VOTING_DEADLINE)).thenReturn(false);
        when(dateService.getCurrentDate()).thenReturn(returningDate);
    }

    private void processMockServicesWhenDeadlinePassed(LocalDate returningDate) {
        when(timeService.getCurrentTime()).thenReturn(PASSED_DEADLINE_TIME);
        when(dateService.getCurrentDate()).thenReturn(returningDate);
    }
}