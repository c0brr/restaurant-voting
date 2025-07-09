package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.repository.DishRepository;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.to.DishTo;
import ru.erulaev.restaurantvoting.user.util.ToConverter;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DishService implements FoodService<Dish, DishTo> {

    private DishRepository dishRepository;
    private MenuRepository menuRepository;

    @Transactional
    public List<DishTo> getAll(long menuId) {
        List<Dish> dishes = dishRepository.getAllByMenuId(menuId);
        if (dishes.isEmpty() && !menuRepository.existsById(menuId)) {
            throw new NotFoundException("Menu with id " + menuId + " not found");
        }
        return dishes.stream()
                .map(dish -> ToConverter.createTo(dish, menuId))
                .toList();
    }

    public DishTo get(long id, long menuId) {
        return ToConverter.createTo(dishRepository.getExisted(id, menuId), menuId);
    }

    @Transactional
    public DishTo save(Dish dish, long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
                new NotFoundException("Menu with id=" + menuId + " not found"));
        dish.setParentEntity(menu);
        return ToConverter.createTo(dishRepository.prepareAndSave(dish), menuId);
    }

    public void delete(long id, long menuId) {
        dishRepository.deleteExisted(id, menuId);
    }

    @Override
    @Transactional
    public void update(Dish newDish, long id, long menuId) {
        Dish oldDish = dishRepository.get(id, menuId).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " not found in menu with id " + menuId));
        oldDish.setPrice(newDish.getPrice());
        oldDish.setName(newDish.getName().toLowerCase());
    }
}