package ru.erulaev.restaurantvoting.user.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.MenuTo;

import java.net.URI;
import java.util.List;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMenuController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    private final MenuService menuService;

    @GetMapping
    public List<MenuTo> getAll(@PathVariable long restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return menuService.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuTo> get(@PathVariable long id, @PathVariable long restaurantId) {
        log.info("get {} from restaurant {}", id, restaurantId);
        return ResponseEntity.of(menuService.get(id, restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable long restaurantId) {
        log.info("create {} for restaurant {}", menu, restaurantId);
        checkNew(menu);
        MenuTo menuTo = menuService.save(menu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, menuTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(menuTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @PathVariable long restaurantId) {
        log.info("delete {} from restaurant {}", id, restaurantId);
        menuService.delete(id, restaurantId);
    }
}