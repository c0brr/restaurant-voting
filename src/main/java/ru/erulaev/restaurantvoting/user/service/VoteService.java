package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.entity.BaseEntity;
import ru.erulaev.restaurantvoting.common.entity.NamedEntity;
import ru.erulaev.restaurantvoting.user.mapper.VoteMapper;
import ru.erulaev.restaurantvoting.user.entity.Restaurant;
import ru.erulaev.restaurantvoting.user.entity.User;
import ru.erulaev.restaurantvoting.user.entity.Vote;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantIdTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteWithRestaurantNameTo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteMapper voteMapper;
    private final TimeService timeService;
    private final DateService dateService;

    @Transactional
    public List<ResponseVoteWithRestaurantNameTo> getAllByUser(int userId) {
        Map<Integer, String> restaurantNamesById = restaurantRepository.getAll().stream()
                .collect(Collectors.toMap(BaseEntity::getId, NamedEntity::getName));
        return voteRepository.getAllByUserId(userId).stream()
                .map(vote -> voteMapper.createResponseWithRestaurantNameTo(vote, restaurantNamesById.get(vote.getRestaurantId())))
                .toList();
    }

    public Optional<ResponseVoteWithRestaurantIdTo> getCurrent(int userId) {
        return voteRepository.getByUserIdAndCreationDate(userId, dateService.getCurrentDate()).map(voteMapper::createResponseWithRestaurantIdTo);
    }

    @Transactional
    public ResponseVoteWithRestaurantIdTo save(RequestVoteTo requestVoteTo, User user) {
        timeService.checkDeadLine();
        Restaurant restaurant = restaurantRepository.getExisted(requestVoteTo.getRestaurantId());
        Vote vote = voteRepository.save(voteMapper.createNewFromRequestTo(requestVoteTo, user, restaurant));
        return voteMapper.createResponseWithRestaurantIdTo(vote);
    }

    @Transactional
    public void deleteCurrent(int userId) {
        timeService.checkDeadLine();
        voteRepository.deleteExisted(userId, dateService.getCurrentDate());
    }

    @Transactional
    public void changeChoice(int restaurantId, int userId) {
        timeService.checkDeadLine();
        Vote vote = voteRepository.getByUserIdAndCreationDate(userId, dateService.getCurrentDate()).orElseThrow(
                () -> new NotFoundException("Vote from user with id=" + userId + " not found for today"));
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        vote.setRestaurant(restaurant);
    }
}