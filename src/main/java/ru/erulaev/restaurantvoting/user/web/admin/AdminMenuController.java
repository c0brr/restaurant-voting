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
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.menu.MenuTo;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueDateMenuValidator;

import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin menu controller", description = "Menu management")
public class AdminMenuController extends AbstractFoodController<Menu, MenuTo> {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    private final UniqueDateMenuValidator dateMenuValidator;

    public AdminMenuController(MenuService service, UniqueDateMenuValidator dateMenuValidator) {
        super(service);
        this.dateMenuValidator = dateMenuValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(dateMenuValidator);
    }

    @Override
    @GetMapping
    @Operation(summary = "To get all menus (by restaurant)", description = "Returns all menus' data by restaurant's ID")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found")
    public List<MenuTo> getAll(@Parameter(description = "Restaurant's ID") @PathVariable long restaurantId) {
        return super.getAll(restaurantId);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "To get menu", description = "Returns menu's data by its ID and restaurant's ID")
    @ApiResponse(responseCode = "200", description = "Menu is found")
    @ApiResponse(responseCode = "404", description = "Menu or restaurant is not found")
    public MenuTo get(@Parameter(description = "Menu's ID") @PathVariable long id,
                      @Parameter(description = "Restaurant's ID") @PathVariable long restaurantId) {
        return super.get(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "To create menu", description = "Creates a new menu")
    @ApiResponse(responseCode = "201", description = "Menu is created")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found")
    public ResponseEntity<MenuTo> createWithLocation(@Parameter(description = "Menu's data") @Valid @RequestBody Menu menu,
                                                     @Parameter(description = "Restaurant's ID") @PathVariable long restaurantId) {
        return super.createWithLocation(menu, restaurantId, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "To delete menu", description = "Deletes a menu by its ID and restaurant's ID")
    @ApiResponse(responseCode = "204", description = "Menu is deleted")
    @ApiResponse(responseCode = "404", description = "Menu or restaurant is not found")
    public void delete(@Parameter(description = "Menu's ID") @PathVariable long id,
                       @Parameter(description = "Restaurant's ID") @PathVariable long restaurantId) {
        super.delete(id, restaurantId);
    }
}