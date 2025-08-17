package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.repository.CoreEntityBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.util.NameUtil;

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
        restaurant.setName(NameUtil.getCorrectName(restaurant.getName()));
        return save(restaurant);
    }

    default Restaurant getExistedByName(String name) {
        return findByNameIgnoreCase(name).orElseThrow(
                () -> new NotFoundException("Restaurant with name=" + name + " not found"));
    }

    @Override
    @Cacheable("restaurants")
    default Restaurant getExisted(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }
}