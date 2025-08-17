package ru.erulaev.restaurantvoting.user.web.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.erulaev.restaurantvoting.user.validation.UniqueRestaurantNameValidator;
import ru.erulaev.restaurantvoting.user.web.response.BodyAndDataApiResponses;
import ru.erulaev.restaurantvoting.user.web.response.SearchResultApiResponses;
import ru.erulaev.restaurantvoting.user.web.response.schema.ProblemDetailSchema;

import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin restaurant controller", description = "Restaurant management")
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
    @Operation(summary = "To get all restaurants",
            description = "Returns all restaurants' data (ID, name, created date), order by name")
    @ApiResponse(responseCode = "200", description = "Request successful")
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "To get restaurant (by ID)",
            description = "Returns restaurant's data (ID, name, created date) by its ID")
    @SearchResultApiResponses
    public Restaurant get(@Parameter(description = "Restaurant's ID") @PathVariable long id) {
        return super.get(id);
    }

    @PostMapping
    @CacheEvict(value = "restaurantList", allEntries = true)
    @Operation(summary = "To create restaurant", description = "Creates a new restaurant")
    @ApiResponse(responseCode = "201", description = "Restaurant is created")
    @BodyAndDataApiResponses
    public ResponseEntity<Restaurant> createWithLocation(@Parameter(description = "Restaurant's data (name)")
                                                         @Valid @RequestBody Restaurant restaurant) {
        return super.createWithLocation(restaurant, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "restaurantList"}, allEntries = true)
    @Operation(summary = "To delete restaurant",
            description = "Deletes a restaurant by its ID. All menus and votes for this restaurant will also be deleted")
    @ApiResponse(responseCode = "204", description = "Restaurant is deleted")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public void delete(@Parameter(description = "Restaurant's ID") @PathVariable long id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "restaurantList"}, allEntries = true)
    @Operation(summary = "To update restaurant", description = "Updates a restaurant by its ID")
    @ApiResponse(responseCode = "204", description = "Restaurant is updated")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @BodyAndDataApiResponses
    public void update(@Parameter(description = "Restaurant's data (name)") @Valid @RequestBody Restaurant restaurant,
                       @Parameter(description = "Restaurant's ID") @PathVariable long id) {
        super.doUpdate(restaurant, id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "To get restaurant (by name)",
            description = "Returns restaurant's data (ID, name, created date) by its name")
    @SearchResultApiResponses
    public Restaurant getByName(@Parameter(description = "Restaurant's name") @RequestParam String name) {
        log.info("getByName {}", name);
        return repository.getExistedByName(name);
    }

    @GetMapping("/by-containing-name")
    @Operation(summary = "To get restaurants by containing name",
            description = "Returns all restaurants' data (ID, name, created date), " +
                    "whose names contain name form request parameter, order by name")
    @ApiResponse(responseCode = "200", description = "Request successful")
    public List<Restaurant> getByContainingName(@Parameter(description = "Name to contain") @RequestParam String name) {
        return super.getByContainingName(name, SORT);
    }
}