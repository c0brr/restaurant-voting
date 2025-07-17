package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.repository.FoodBaseRepository;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.util.NameUtil;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends FoodBaseRepository<Dish> {

    List<Dish> getAllByMenuId(long menuId);

    Optional<Dish> getByMenuIdAndName(long menuId, String name);

    @Transactional
    default Dish prepareAndSave(Dish dish) {
        dish.setName(NameUtil.getCorrectName(dish.getName()));
        return save(dish);
    }
}