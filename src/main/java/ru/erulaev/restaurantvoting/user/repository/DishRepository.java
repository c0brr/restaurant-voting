package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.erulaev.restaurantvoting.common.repository.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.entity.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends FoodBaseRepository<Dish> {

    List<Dish> getAllByMenuId(int menuId);

    Optional<Dish> getByMenuIdAndName(int menuId, String name);

    @Transactional
    default Dish prepareAndSave(Dish dish) {
        dish.setName(StringUtils.capitalize(dish.getName()));
        return save(dish);
    }
}