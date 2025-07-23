package ru.erulaev.restaurantvoting.user.web.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin dish controller", description = "Dish management")
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
    @Operation(summary = "To get all dishes (by menu)", description = "Returns all dishes' data by menu's ID")
    @ApiResponse(responseCode = "404", description = "Menu is not found")
    public List<DishTo> getAll(@Parameter(description = "Menu's ID") @PathVariable long menuId) {
        return super.getAll(menuId);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "To get dish", description = "Returns dish's data by its ID and menu's ID")
    @ApiResponse(responseCode = "200", description = "Dish is found")
    @ApiResponse(responseCode = "404", description = "Dish or menu is not found")
    public DishTo get(@Parameter(description = "Dish's ID") @PathVariable long id,
                      @Parameter(description = "Menu's ID") @PathVariable long menuId) {
        return super.get(id, menuId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "To create dish", description = "Creates a new dish")
    @ApiResponse(responseCode = "201", description = "Dish is created")
    @ApiResponse(responseCode = "404", description = "Menu is not found")
    public ResponseEntity<DishTo> createWithLocation(@Parameter(description = "Dish's data") @Valid @RequestBody Dish dish,
                                                     @Parameter(description = "Menu's ID") @PathVariable long menuId) {
        return super.createWithLocation(dish, menuId, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "To delete dish", description = "Deletes a dish by its ID and menu's ID")
    @ApiResponse(responseCode = "204", description = "Dish is deleted")
    @ApiResponse(responseCode = "404", description = "Dish or menu is not found")
    public void delete(@Parameter(description = "Dish's ID") @PathVariable long id,
                       @Parameter(description = "Menu's ID") @PathVariable long menuId) {
        super.delete(id, menuId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "To update dish", description = "Updates a dish by its ID and menu's ID")
    @ApiResponse(responseCode = "204", description = "Dish is updated")
    @ApiResponse(responseCode = "404", description = "Dish or menu is not found")
    public void update(@Parameter(description = "Dish's data") @Valid @RequestBody Dish dish,
                       @Parameter(description = "Dish's ID") @PathVariable long id,
                       @Parameter(description = "Menu's ID") @PathVariable long menuId) {
        log.info("update {} with id={} from menu {}", dish, id, menuId);
        assureIdConsistent(dish, id);
        service.update(dish, id, menuId);
    }
}