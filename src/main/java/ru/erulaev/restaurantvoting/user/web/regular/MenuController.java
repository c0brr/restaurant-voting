package ru.erulaev.restaurantvoting.user.web.regular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.MenuTo;

import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class MenuController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/menus";

    private final MenuService menuService;

    @GetMapping
    public List<MenuTo> getAll(@PathVariable long restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return menuService.getAll(restaurantId);
    }

    @GetMapping("{id}")
    public MenuTo get(@PathVariable long id, @PathVariable long restaurantId) {
        log.info("get {} from restaurant {}", id, restaurantId);
        return menuService.get(id, restaurantId);
    }
}
