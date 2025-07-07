package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Cacheable("restaurants")
    List<Restaurant> getAll();

    List<Restaurant> findByNameContainingIgnoreCase(String name, Sort sort);

    Optional<Restaurant> findByNameIgnoreCase(String name);

    @Transactional
    @Modifying
    int delete(long id);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(long id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Restaurant with id=" + id + " not found");
        }
    }

    @Transactional
    default Restaurant prepareAndSave(Restaurant restaurant) {
        restaurant.setName(restaurant.getName().toLowerCase());
        return save(restaurant);
    }
}