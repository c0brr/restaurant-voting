package ru.erulaev.restaurantvoting.user.web.admin;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.Restaurant;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueRestaurantNameValidator;

import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController extends AbstractCoreEntityController<Restaurant, RestaurantRepository> {

    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name");
    static final String REST_URL = "/api/admin/restaurants";

    private final UniqueRestaurantNameValidator nameValidator;

    public AdminRestaurantController(RestaurantRepository repository, UniqueRestaurantNameValidator nameValidator) {
        super(repository);
        this.nameValidator = nameValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(nameValidator);
    }

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        return super.get(id);
    }

    @PostMapping
    @CacheEvict(value = "restaurantList", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        return super.createWithLocation(restaurant, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "restaurantList"}, allEntries = true)
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "restaurantList"}, allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable long id) {
        super.update(restaurant, id);
    }

    @GetMapping("/by-name")
    public Restaurant getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return repository.getExistedByName(name);
    }

    @GetMapping("/by-containing-name")
    public List<Restaurant> getByContainingName(@RequestParam String name) {
        return super.getByContainingName(name, SORT);
    }
}