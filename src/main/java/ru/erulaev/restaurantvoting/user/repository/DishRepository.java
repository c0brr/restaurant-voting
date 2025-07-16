package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.repository.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends FoodBaseRepository<Dish> {

    List<Dish> getAllByMenuId(long menuId);

    @Transactional
    default Dish prepareAndSave(Dish dish) {
        String name = dish.getName();
        dish.setName(Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase());
        return save(dish);
    }
}