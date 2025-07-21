package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.mapper.DishMapper;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.repository.DishRepository;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService implements FoodService<Dish, DishTo> {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final DishMapper dishMapper;

    @Override
    @Transactional
    public List<DishTo> getAll(long menuId) {
        List<Dish> dishes = dishRepository.getAllByMenuId(menuId);
        if (dishes.isEmpty() && !menuRepository.existsById(menuId)) {
            throw new NotFoundException("Menu with id=" + menuId + " not found");
        }
        return dishes.stream()
                .map(dishMapper::createTo)
                .toList();
    }

    @Override
    public DishTo get(long id, long menuId) {
        return dishMapper.createTo(dishRepository.getExisted(id, menuId));
    }

    @Override
    @Transactional
    public DishTo save(Dish dish, long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new NotFoundException("Menu with id=" + menuId + " not found"));
        dish.setParentEntity(menu);
        return dishMapper.createTo(dishRepository.prepareAndSave(dish));
    }

    @Override
    public void delete(long id, long menuId) {
        dishRepository.deleteExisted(id, menuId);
    }

    @Override
    @Transactional
    public void update(Dish newDish, long id, long menuId) {
        Dish oldDish = dishRepository.getExisted(id, menuId);
        oldDish.setPrice(newDish.getPrice());
        String newName = newDish.getName();
        oldDish.setName(Character.toUpperCase(newName.charAt(0)) + newName.substring(1).toLowerCase());
    }
}