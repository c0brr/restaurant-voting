package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.erulaev.restaurantvoting.user.entity.Restaurant;
import ru.erulaev.restaurantvoting.user.entity.User;
import ru.erulaev.restaurantvoting.user.entity.Vote;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantIdTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantNameTo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoteMapper {

    @Mapping(target = "id", ignore = true)
    Vote createNewFromRequestTo(RequestVoteTo requestVoteTo, User user, Restaurant restaurant);

    ResponseVoteWithRestaurantIdTo createResponseWithRestaurantIdTo(Vote vote);

    ResponseVoteWithRestaurantNameTo createResponseWithRestaurantNameTo(Vote vote, String restaurantName);
}
