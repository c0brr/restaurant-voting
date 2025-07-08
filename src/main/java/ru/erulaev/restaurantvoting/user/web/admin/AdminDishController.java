package ru.erulaev.restaurantvoting.user.web.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.service.DishService;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.util.List;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController extends AbstractFoodController<Dish, DishTo> {

    static final String REST_URL = "/api/admin/menus/{menuId}/dishes";

    public AdminDishController(DishService service) {
        super(service);
    }

    @GetMapping
    public List<DishTo> getAll(@PathVariable long menuId) {
        return super.getAll(menuId);
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable long id, @PathVariable long menuId) {
        return super.get(id, menuId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable long menuId) {
        return super.createWithLocation(dish, menuId, REST_URL);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @PathVariable long menuId) {
        super.delete(id, menuId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable long id, @PathVariable long menuId) {
        log.info("update {} with id={} from menu {}", dish, id, menuId);
        assureIdConsistent(dish, id);
        service.update(dish, id, menuId);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Dish> getByName(@RequestParam String name, @PathVariable long menuId) {
        log.info("getByName {} from menu {}", name, menuId);
        return ResponseEntity.of(service.getByName(name, menuId));
    }
}