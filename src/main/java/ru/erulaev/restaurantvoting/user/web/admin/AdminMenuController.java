package ru.erulaev.restaurantvoting.user.web.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.MenuTo;

import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends AbstractFoodController<Menu, MenuTo> {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    public AdminMenuController(MenuService service) {
        super(service);
    }

    @GetMapping
    public List<MenuTo> getAll(@PathVariable long restaurantId) {
        return super.getAll(restaurantId);
    }

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable long id, @PathVariable long restaurantId) {
        return super.get(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable long restaurantId) {
        return super.createWithLocation(menu, restaurantId, REST_URL);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @PathVariable long restaurantId) {
        super.delete(id, restaurantId);
    }
}