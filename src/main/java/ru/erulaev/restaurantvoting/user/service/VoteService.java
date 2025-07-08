package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.DataConflictException;
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

@Service
@AllArgsConstructor
public class VoteService {

    private static final LocalTime VOTING_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private RestaurantRepository restaurantRepository;

    @Transactional
    public ResponseVoteTo save(RequestVoteTo requestVoteTo, User user) {
        checkDeadLine();
        Restaurant restaurant = getRestaurant(requestVoteTo.getRestaurantId());
        Vote vote = voteRepository.save(ToConverter.createNewFromRequestTo(requestVoteTo, user, restaurant));
        return ToConverter.createResponseTo(vote);
    }

    @Transactional
    public void delete(long id, User user) {
        checkDeadLine();
        checkCurrentDate(getVote(id, user.id()));
        voteRepository.deleteById(id);
    }

    @Transactional
    public void update(RequestVoteTo requestVoteTo, User user) {
        checkDeadLine();
        Vote oldVote = getVote(requestVoteTo.getId(), user.id());
        checkCurrentDate(oldVote);
        Restaurant restaurant = getRestaurant(requestVoteTo.getRestaurantId());
        oldVote.setCreated(requestVoteTo.getDate());
        oldVote.setRestaurant(restaurant);
    }

    private void checkDeadLine() {
        if (LocalTime.now().isAfter(VOTING_DEADLINE)) {
            throw new DataConflictException("Voting is over for today");
        }
    }

    private Vote getVote(long id, long userId) {
        return voteRepository.get(id, userId).orElseThrow(
                () -> new NotFoundException("Vote with id=" + id + " from user with id=" + userId + " not found"));
    }

    private void checkCurrentDate(Vote vote) {
        if (!vote.getCreated().isEqual(LocalDate.now())) {
            throw new DataConflictException("You can't change your votes for past days");
        }
    }

    private Restaurant getRestaurant(long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
    }
}