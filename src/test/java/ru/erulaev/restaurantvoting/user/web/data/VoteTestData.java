package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.common.to.BaseTo;
import ru.erulaev.restaurantvoting.user.mapper.VoteMapper;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.erulaev.restaurantvoting.user.web.data.RestaurantTestData.*;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.user1;
import static ru.erulaev.restaurantvoting.user.web.data.UserTestData.user2;

public class VoteTestData {

    public static final MatcherFactory.Matcher<ResponseVoteTo> RESPONSE_VOTE_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ResponseVoteTo.class);
    public static final VoteMapper MAPPER = Mappers.getMapper(VoteMapper.class);

    public static final long VOTE_1_ID = 1L;
    public static final long VOTE_2_ID = 2L;
    public static final long VOTE_3_ID = 3L;
    public static final long VOTE_4_ID = 4L;
    public static final long VOTE_5_ID = 5L;

    public static final LocalDate DATE_NOT_FOUND = LocalDate.of(2024, 1, 1);
    public static final LocalDate CORRECT_DATE = LocalDate.of(2025, 2, 2);
    public static final LocalTime PASSED_DEADLINE_TIME = LocalTime.of(13, 0);

    public static final ResponseVoteTo responseVoteTo1 =
            getResponseTo(new Vote(VOTE_1_ID, LocalDate.of(2025, 7, 7), user1, restaurant2));
    public static final ResponseVoteTo responseVoteTo2 =
            getResponseTo(new Vote(VOTE_2_ID, LocalDate.of(2025, 7, 28), user1, restaurant2));
    public static final ResponseVoteTo responseVoteTo3 =
            getResponseTo(new Vote(VOTE_3_ID, LocalDate.of(2025, 7, 29), user1, restaurant1));
    public static final ResponseVoteTo responseVoteTo4 =
            getResponseTo(new Vote(VOTE_4_ID, LocalDate.of(2025, 7, 30), user1, restaurant3));
    public static final ResponseVoteTo responseVoteTo5 =
            getResponseTo(new Vote(VOTE_5_ID, LocalDate.of(2025, 7, 7), user2, restaurant1));

    public static ResponseVoteTo getResponseTo(Vote vote) {
        return MAPPER.createResponseTo(vote);
    }

    public static void matchVotes(ResponseVoteTo responseVoteTo, RequestVoteTo requestVoteTo, long userId) {
        assertThat(responseVoteTo).extracting(BaseTo::getId,
                        ResponseVoteTo::getDate,
                        ResponseVoteTo::getRestaurantId)
                .containsExactly(requestVoteTo.getId(),
                        requestVoteTo.getDate(),
                        requestVoteTo.getRestaurantId());
        assertEquals(userId, responseVoteTo.getUserId());
    }
}
