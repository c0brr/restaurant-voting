package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteToWithRestaurantId;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteToWithRestaurantName;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoteMapper {

    @Mapping(target = "id", ignore = true)
    Vote createNewFromRequestTo(RequestVoteTo requestVoteTo, User user, Restaurant restaurant);

    ResponseVoteToWithRestaurantId createResponseToWithRestaurantId(Vote vote);

    ResponseVoteToWithRestaurantName createResponseToWithRestaurantName(Vote vote, String restaurantName);
}
