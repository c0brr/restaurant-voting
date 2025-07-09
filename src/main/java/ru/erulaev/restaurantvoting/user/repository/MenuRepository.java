package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.repository.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Menu;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends FoodBaseRepository<Menu> {

    List<Menu> getAllByRestaurantId(long restaurantId);
}