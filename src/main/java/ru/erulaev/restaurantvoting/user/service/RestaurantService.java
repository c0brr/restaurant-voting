package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
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

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public List<RestaurantTo> getAll() {
        Map<Long, Integer> votesByRestaurant = voteRepository.findAllByDate(LocalDate.now()).stream()
                .collect(Collectors.toMap(Vote::getRestaurantId, vote -> 1, Integer::sum));
        return restaurantRepository.getAll().stream()
                .map(restaurant ->
                        RestaurantUtil.createTo(restaurant, votesByRestaurant.getOrDefault(restaurant.getId(), 0)))
                .toList();
    }

    @Transactional
    public RestaurantTo get(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Restaurant with id=" + id + " not found"));
        return RestaurantUtil.createTo(restaurant,
                voteRepository.findByDateAndRestaurantId(LocalDate.now(), id));
    }

    @Transactional
    public RestaurantTo getByName(String name) {
        Restaurant restaurant = restaurantRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new NotFoundException("Restaurant with name=" + name + " not found"));
        return RestaurantUtil.createTo(restaurant,
                voteRepository.findByDateAndRestaurantId(LocalDate.now(), restaurant.getId()));
    }
}