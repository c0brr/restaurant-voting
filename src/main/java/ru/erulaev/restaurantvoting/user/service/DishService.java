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
import ru.erulaev.restaurantvoting.user.util.DishesUtil;

import java.util.List;
import java.util.Optional;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;

@Service
@AllArgsConstructor
public class DishService {

    private DishRepository dishRepository;
    private MenuRepository menuRepository;

    @Transactional
    public List<DishTo> getAll(long menuId) {
        List<Dish> dishes = dishRepository.getAll(menuId);
        if (dishes.isEmpty() && !menuRepository.existsById(menuId)) {
            throw new NotFoundException("Menu with id " + menuId + " not found");
        }
        return dishes.stream()
                .map(dish -> DishesUtil.createTo(dish, menuId))
                .toList();
    }

    public Optional<DishTo> get(long id, long menuId) {
        return dishRepository.getById(id, menuId).map(dish -> DishesUtil.createTo(dish, menuId));
    }

    @Transactional
    public DishTo save(Dish dish, long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
                new NotFoundException("Menu with id=" + menuId + " not found"));
        dish.setMenu(menu);
        return DishesUtil.createTo(dishRepository.prepareAndSave(dish), menuId);
    }

    public void delete(long id, long menuId) {
        dishRepository.deleteExisted(id, menuId);
    }

    @Transactional
    public void update(Dish newDish, long id, long menuId) {
        Dish oldDish = dishRepository.getById(id, menuId).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " not found in menu with id " + menuId));
        oldDish.setName(newDish.getName().toLowerCase());
        oldDish.setPrice(newDish.getPrice());
    }

    public Optional<Dish> getByName(String name, long menuId) {
        return dishRepository.findByNameIgnoreCase(name, menuId);
    }
}