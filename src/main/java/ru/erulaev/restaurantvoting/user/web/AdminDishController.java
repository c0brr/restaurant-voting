package ru.erulaev.restaurantvoting.user.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.service.DishService;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.net.URI;
import java.util.List;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminDishController {

    static final String REST_URL = "/api/admin/menus/{menuId}/dishes";

    private final DishService dishService;

    @GetMapping
    public List<DishTo> getAll(@PathVariable long menuId) {
        log.info("getAll for menu {}", menuId);
        return dishService.getAll(menuId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishTo> get(@PathVariable long id, @PathVariable long menuId) {
        log.info("get {} from menu {}", id, menuId);
        return ResponseEntity.of(dishService.get(id, menuId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable long menuId) {
        log.info("create {} for menu {}", dish, menuId);
        checkNew(dish);
        DishTo dishTo = dishService.save(dish, menuId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(menuId, dishTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(dishTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @PathVariable long menuId) {
        log.info("delete {} from menu {}", id, menuId);
        dishService.delete(id, menuId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable long id, @PathVariable long menuId) {
        log.info("update {} with id={} from menu {}", dish, id, menuId);
        assureIdConsistent(dish, id);
        dishService.update(dish, id, menuId);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Dish> getByName(@RequestParam String name, @PathVariable long menuId) {
        log.info("getByName {} from menu {}", name, menuId);
        return ResponseEntity.of(dishService.getByName(name, menuId));
    }
}