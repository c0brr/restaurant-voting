package ru.erulaev.restaurantvoting.user.web.data;

import org.mapstruct.factory.Mappers;
import ru.erulaev.restaurantvoting.MatcherFactory;
import ru.erulaev.restaurantvoting.user.mapper.VoteMapper;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;

import java.time.LocalDate;

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

    public static final ResponseVoteTo responseVoteTo1 =
            MAPPER.createResponseTo(new Vote(VOTE_1_ID, LocalDate.of(2025, 7, 7), user1, restaurant2));
    public static final ResponseVoteTo responseVoteTo2 =
            MAPPER.createResponseTo(new Vote(VOTE_2_ID, LocalDate.of(2025, 7, 28), user1, restaurant2));
    public static final ResponseVoteTo responseVoteTo3 =
            MAPPER.createResponseTo(new Vote(VOTE_3_ID, LocalDate.of(2025, 7, 29), user1, restaurant1));
    public static final ResponseVoteTo responseVoteTo4 =
            MAPPER.createResponseTo(new Vote(VOTE_4_ID, LocalDate.of(2025, 7, 30), user1, restaurant3));
    public static final ResponseVoteTo responseVoteTo5 =
            MAPPER.createResponseTo(new Vote(VOTE_5_ID, LocalDate.of(2025, 7, 7), user2, restaurant1));
}
