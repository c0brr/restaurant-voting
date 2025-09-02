package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.common.to.BaseTo;
import ru.erulaev.restaurantvoting.user.mapper.VoteMapper;
import ru.erulaev.restaurantvoting.user.entity.Vote;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantIdTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantNameTo;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.user1;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.user2;

public class VoteTestData {

    public static final MatcherFactory.Matcher<ResponseVoteWithRestaurantIdTo> RESPONSE_VOTE_WITH_RESTAURANT_ID_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ResponseVoteWithRestaurantIdTo.class);
    public static final MatcherFactory.Matcher<ResponseVoteWithRestaurantNameTo> RESPONSE_VOTE_WITH_RESTAURANT_NAME_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ResponseVoteWithRestaurantNameTo.class);
    public static final VoteMapper MAPPER = Mappers.getMapper(VoteMapper.class);

    public static final int VOTE_1_ID = 1;
    public static final int VOTE_2_ID = 2;
    public static final int VOTE_3_ID = 3;
    public static final int VOTE_4_ID = 4;
    public static final int VOTE_5_ID = 5;

    public static final LocalDate DATE_NOT_FOUND = LocalDate.of(2024, 1, 1);
    public static final LocalDate CORRECT_DATE = LocalDate.of(2025, 2, 2);
    public static final LocalTime PASSED_DEADLINE_TIME = LocalTime.of(13, 0);

    public static final ResponseVoteWithRestaurantNameTo RESPONSE_VOTE_TO_WITH_RESTAURANT_NAME_1 =
            getResponseWithRestaurantNameTo(new Vote(VOTE_1_ID, LocalDate.of(2025, 7, 7), user1, restaurant2), RESTAURANT_2_NAME);
    public static final ResponseVoteWithRestaurantNameTo RESPONSE_VOTE_TO_WITH_RESTAURANT_NAME_2 =
            getResponseWithRestaurantNameTo(new Vote(VOTE_2_ID, LocalDate.of(2025, 7, 28), user1, restaurant2), RESTAURANT_2_NAME);
    public static final ResponseVoteWithRestaurantNameTo RESPONSE_VOTE_TO_WITH_RESTAURANT_NAME_3 =
            getResponseWithRestaurantNameTo(new Vote(VOTE_3_ID, LocalDate.of(2025, 7, 29), user1, restaurant1), RESTAURANT_1_NAME);
    public static final ResponseVoteWithRestaurantNameTo RESPONSE_VOTE_TO_WITH_RESTAURANT_NAME_4 =
            getResponseWithRestaurantNameTo(new Vote(VOTE_4_ID, LocalDate.of(2025, 7, 30), user1, restaurant3), RESTAURANT_3_NAME);

    public static final ResponseVoteWithRestaurantIdTo RESPONSE_VOTE_TO_WITH_RESTAURANT_ID_5 =
            getResponseWithRestaurantIdTo(new Vote(VOTE_5_ID, LocalDate.of(2025, 7, 7), user2, restaurant1));

    public static ResponseVoteWithRestaurantIdTo getResponseWithRestaurantIdTo(Vote vote) {
        return MAPPER.createResponseWithRestaurantIdTo(vote);
    }

    public static ResponseVoteWithRestaurantNameTo getResponseWithRestaurantNameTo(Vote vote, String restaurantName) {
        return MAPPER.createResponseWithRestaurantNameTo(vote, restaurantName);
    }

    public static void matchVotes(ResponseVoteWithRestaurantIdTo responseVoteWithRestaurantIdTo, RequestVoteTo requestVoteTo, int userId) {
        assertThat(responseVoteWithRestaurantIdTo).extracting(BaseTo::getId,
                        ResponseVoteWithRestaurantIdTo::getCreationDate,
                        ResponseVoteWithRestaurantIdTo::getRestaurantId)
                .containsExactly(requestVoteTo.getId(),
                        requestVoteTo.getCreationDate(),
                        requestVoteTo.getRestaurantId());
        assertEquals(userId, responseVoteWithRestaurantIdTo.getUserId());
    }
}
