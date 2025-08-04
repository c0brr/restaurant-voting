package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.mapper.RestaurantMapper;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.model.Vote;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.to.RestaurantTo;
import ru.erulaev.restaurantvoting.user.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantService {

    private static final Integer ONE_VOTE = 1;
    private static final int ZERO_VOTES = 0;

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final RestaurantMapper restaurantMapper;
    private final DateService dateService;

    @Transactional
    public List<RestaurantTo> getAllByDate(LocalDate date) {
        Map<Long, Integer> votesByRestaurant = voteRepository.findAllByDate(date)
                .stream()
                .collect(Collectors.toMap(Vote::getRestaurantId, vote -> ONE_VOTE, Integer::sum));

        List<RestaurantTo> restaurantTos = restaurantRepository.getAll().stream()
                .filter(restaurant -> RestaurantUtil.isRestaurantExistedAtDate(restaurant, date))
                .map(restaurant ->
                        restaurantMapper.createTo(restaurant, votesByRestaurant.getOrDefault(restaurant.getId(), ZERO_VOTES)))
                .toList();
        if (restaurantTos.isEmpty()) {
            throw new NotFoundException("No data available for this date");
        }
        return restaurantTos;
    }

    @Transactional
    public RestaurantTo get(long id) {
        Restaurant restaurant = restaurantRepository.getExisted(id);
        return restaurantMapper.createTo(restaurant,
                voteRepository.getCountByDateAndRestaurantId(dateService.getCurrentDate(), id));
    }

    @Transactional
    public RestaurantTo getByName(String name) {
        Restaurant restaurant = restaurantRepository.getExistedByName(name);
        return restaurantMapper.createTo(restaurant,
                voteRepository.getCountByDateAndRestaurantId(dateService.getCurrentDate(), restaurant.getId()));
    }
}