package ru.erulaev.restaurantvoting.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.mapper.MenuMapper;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.to.menu.AdminMenuTo;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;
import ru.erulaev.restaurantvoting.user.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService implements FoodService<Menu, AdminMenuTo> {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuMapper menuMapper;

    @Override
    @Transactional
    public List<AdminMenuTo> getAll(long restaurantId) {
        List<Menu> menus = menuRepository.getAllByRestaurantId(restaurantId);
        if (menus.isEmpty() && !restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Restaurant with id " + restaurantId + " not found");
        }
        return menus.stream()
                .map(menuMapper::createTo)
                .toList();
    }

    @Override
    public AdminMenuTo get(long id, long restaurantId) {
        return menuMapper.createTo(menuRepository.getExisted(id, restaurantId));
    }

    @Override
    @Transactional
    public AdminMenuTo save(Menu menu, long restaurantId) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        menu.setParentEntity(restaurant);
        return menuMapper.createTo(menuRepository.save(menu));
    }

    @Override
    public void delete(long id, long restaurantId) {
        menuRepository.deleteExisted(id, restaurantId);
    }

    @Transactional
    public Optional<RegularMenuTo> getByDate(long restaurantId, LocalDate date) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        if (!RestaurantUtil.isRestaurantExistedAtDate(restaurant, date)) {
            throw new NotFoundException("No data available for date " + date);
        }
        return menuRepository.getWithDishesByDate(restaurantId, date).map(menuMapper::createToWithDishes);
    }
}