package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.repository.CoreEntityBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends CoreEntityBaseRepository<Restaurant> {

    @Override
    @Cacheable("restaurantList")
    List<Restaurant> getAll();

    Optional<Restaurant> findByNameIgnoreCase(String name);

    @Override
    @Transactional
    default Restaurant prepareAndSave(Restaurant restaurant) {
        String name = restaurant.getName();
        restaurant.setName(Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase());
        return save(restaurant);
    }

    default Restaurant getExistedByName(String name) {
        return findByNameIgnoreCase(name).orElseThrow(
                () -> new NotFoundException("Restaurant with name=" + name + " not found"));
    }

    @Override
    @Cacheable("restaurants")
    default Restaurant getExisted(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }
}