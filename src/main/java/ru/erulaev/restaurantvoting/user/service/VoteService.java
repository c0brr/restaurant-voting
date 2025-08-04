package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.IllegalRequestDataException;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.mapper.VoteMapper;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    public static final LocalTime VOTING_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteMapper voteMapper;
    private final TimeService timeService;
    private final DateService dateService;

    public List<ResponseVoteTo> getAllByUser(long userId) {
        return voteRepository.getAllByUserId(userId).stream()
                .map(voteMapper::createResponseTo)
                .toList();
    }

    public Optional<ResponseVoteTo> getCurrent(long userId) {
        return voteRepository.getByUserIdAndDate(userId, dateService.getCurrentDate()).map(voteMapper::createResponseTo);
    }

    @Transactional
    public ResponseVoteTo save(RequestVoteTo requestVoteTo, User user) {
        checkDeadLine();
        Restaurant restaurant = restaurantRepository.getExisted(requestVoteTo.getRestaurantId());
        Vote vote = voteRepository.save(voteMapper.createNewFromRequestTo(requestVoteTo, user, restaurant));
        return voteMapper.createResponseTo(vote);
    }

    @Transactional
    public void deleteCurrent(long userId) {
        checkDeadLine();
        voteRepository.deleteExisted(userId, dateService.getCurrentDate());
    }

    @Transactional
    public void changeChoice(long restaurantId, long userId) {
        checkDeadLine();
        Vote vote = voteRepository.getByUserIdAndDate(userId, dateService.getCurrentDate()).orElseThrow(
                () -> new NotFoundException("Vote from user with id=" + userId + " not found for today"));
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        vote.setRestaurant(restaurant);
    }

    private void checkDeadLine() {
        if (timeService.isDeadLinePassed(VOTING_DEADLINE)) {
            throw new IllegalRequestDataException("Voting is over for today");
        }
    }
}