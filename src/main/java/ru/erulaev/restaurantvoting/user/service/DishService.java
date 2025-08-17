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
import ru.erulaev.restaurantvoting.user.to.dish.DishToWithMenuId;
import ru.erulaev.restaurantvoting.user.util.NameUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService implements FoodService<Dish, DishToWithMenuId> {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final DishMapper dishMapper;

    @Override
    @Transactional
    public List<DishToWithMenuId> getAll(int menuId) {
        List<Dish> dishes = dishRepository.getAllByMenuId(menuId);
        if (dishes.isEmpty() && !menuRepository.existsById(menuId)) {
            throw new NotFoundException("Menu with id=" + menuId + " not found");
        }
        return dishes.stream()
                .map(dishMapper::createToWithMenuId)
                .toList();
    }

    @Override
    public DishToWithMenuId get(int id, int menuId) {
        return dishMapper.createToWithMenuId(dishRepository.getExisted(id, menuId));
    }

    @Override
    @Transactional
    public DishToWithMenuId save(Dish dish, int menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new NotFoundException("Menu with id=" + menuId + " not found"));
        dish.setParentEntity(menu);
        return dishMapper.createToWithMenuId(dishRepository.prepareAndSave(dish));
    }

    @Override
    public void delete(int id, int menuId) {
        dishRepository.deleteExisted(id, menuId);
    }

    @Override
    @Transactional
    public void update(Dish newDish, int id, int menuId) {
        Dish oldDish = dishRepository.getExisted(id, menuId);
        oldDish.setPrice(newDish.getPrice());
        oldDish.setName(NameUtil.getCorrectName(newDish.getName()));
    }
}