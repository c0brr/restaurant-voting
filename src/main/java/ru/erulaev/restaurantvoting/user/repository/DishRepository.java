package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends FoodBaseRepository<Dish> {

    Optional<Dish> getByNameIgnoreCaseAndMenuId(String name, long menuId);

    List<Dish> getAllByMenuId(long menuId);

    @Transactional
    default Dish prepareAndSave(Dish dish) {
        dish.setName(dish.getName().toLowerCase());
        return save(dish);
    }
}