package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.IllegalRequestDataException;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.to.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.ResponseVoteTo;
import ru.erulaev.restaurantvoting.user.util.ToConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private static final LocalTime VOTING_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private RestaurantRepository restaurantRepository;

    public List<ResponseVoteTo> getAllByUser(long userId) {
        return voteRepository.getAllByUserId(userId).stream()
                .map(ToConverter::createResponseTo)
                .toList();
    }

    public Optional<ResponseVoteTo> getCurrent(long userId) {
        return voteRepository.getByUserIdAndDate(userId, LocalDate.now()).map(ToConverter::createResponseTo);
    }

    @Transactional
    public ResponseVoteTo save(RequestVoteTo requestVoteTo, User user) {
        checkDeadLine();
        Restaurant restaurant = restaurantRepository.getExisted(requestVoteTo.getRestaurantId());
        Vote vote = voteRepository.save(ToConverter.createNewFromRequestTo(requestVoteTo, user, restaurant));
        return ToConverter.createResponseTo(vote);
    }

    @Transactional
    public void deleteCurrent(long userId) {
        checkDeadLine();
        voteRepository.deleteExisted(userId, LocalDate.now());
    }

    @Transactional
    public void changeChoice(long restaurantId, long userId) {
        checkDeadLine();
        Vote vote = voteRepository.getByUserIdAndDate(userId, LocalDate.now()).orElseThrow(
                () -> new NotFoundException("Vote from user with id=" + userId + " not found for today"));
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        vote.setRestaurant(restaurant);
    }

    private void checkDeadLine() {
        if (LocalTime.now().isAfter(VOTING_DEADLINE)) {
            throw new IllegalRequestDataException("Voting is over for today");
        }
    }
}