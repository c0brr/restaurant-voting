package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.repository.CoreEntityBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends CoreEntityBaseRepository<Restaurant> {

    @Override
    @Cacheable("restaurants")
    List<Restaurant> getAll();

    Optional<Restaurant> findByNameIgnoreCase(String name);

    @Override
    @Transactional
    default Restaurant prepareAndSave(Restaurant restaurant) {
        restaurant.setName(restaurant.getName().toLowerCase());
        return save(restaurant);
    }
}