package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.to.MenuTo;
import ru.erulaev.restaurantvoting.user.util.ToConverter;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuService implements FoodService<Menu, MenuTo> {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public List<MenuTo> getAll(long restaurantId) {
        restaurantRepository.getExisted(restaurantId);
        List<Menu> menus = menuRepository.getAllByRestaurantId(restaurantId);
        return menus.stream()
                .map(menu -> ToConverter.createTo(menu, restaurantId))
                .toList();
    }

    public MenuTo get(long id, long restaurantId) {
        return ToConverter.createTo(menuRepository.getExisted(id, restaurantId), restaurantId);
    }

    @Transactional
    public MenuTo save(Menu menu, long restaurantId) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        menu.setParentEntity(restaurant);
        return ToConverter.createTo(menuRepository.save(menu), restaurantId);
    }

    public void delete(long id, long restaurantId) {
        menuRepository.deleteExisted(id, restaurantId);
    }
}