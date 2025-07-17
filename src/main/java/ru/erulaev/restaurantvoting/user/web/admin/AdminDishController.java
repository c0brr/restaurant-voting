package ru.erulaev.restaurantvoting.user.web.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.service.DishService;
import ru.erulaev.restaurantvoting.user.to.DishTo;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueDishNameValidator;

import java.util.List;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController extends AbstractFoodController<Dish, DishTo> {

    static final String REST_URL = "/api/admin/menus/{menuId}/dishes";

    private final UniqueDishNameValidator dishNameValidator;

    public AdminDishController(DishService service, UniqueDishNameValidator dishNameValidator) {
        super(service);
        this.dishNameValidator = dishNameValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(dishNameValidator);
    }

    @Override
    @GetMapping
    public List<DishTo> getAll(@PathVariable long menuId) {
        return super.getAll(menuId);
    }

    @Override
    @GetMapping("/{id}")
    public DishTo get(@PathVariable long id, @PathVariable long menuId) {
        return super.get(id, menuId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable long menuId) {
        return super.createWithLocation(dish, menuId, REST_URL);
    }

    @Override
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
}