package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.repository.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends FoodBaseRepository<Menu> {

    List<Menu> getAllByRestaurantId(long restaurantId);

    Optional<Menu> getWithDishesByDate(long restaurantId, LocalDate date);

    Optional<Menu> getByRestaurantIdAndDate(long restaurantId, LocalDate date);
}