package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.to.menu.MenuTo;
import ru.erulaev.restaurantvoting.user.util.ToConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService implements FoodService<Menu, MenuTo> {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public List<MenuTo> getAll(long restaurantId) {
        List<Menu> menus = menuRepository.getAllByRestaurantId(restaurantId);
        if (menus.isEmpty() && !restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Restaurant with id " + restaurantId + " not found");
        }
        return menus.stream()
                .map(menu -> ToConverter.createTo(menu, restaurantId))
                .toList();
    }

    @Override
    public MenuTo get(long id, long restaurantId) {
        return ToConverter.createTo(menuRepository.getExisted(id, restaurantId), restaurantId);
    }

    @Override
    @Transactional
    public MenuTo save(Menu menu, long restaurantId) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        menu.setParentEntity(restaurant);
        return ToConverter.createTo(menuRepository.save(menu), restaurantId);
    }

    @Override
    public void delete(long id, long restaurantId) {
        menuRepository.deleteExisted(id, restaurantId);
    }

    @Transactional
    public Optional<MenuTo> getByDate(long restaurantId, LocalDate date) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Restaurant with id " + restaurantId + " not found");
        }
        return menuRepository.getWithDishesByDate(restaurantId, date).map(ToConverter::createToWIthDishes);
    }
}