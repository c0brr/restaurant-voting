package ru.erulaev.restaurantvoting.user.web.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin user controller", description = "User management")
public class AdminUserController extends AbstractCoreEntityController<User, UserRepository> {

    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name", "email");
    static final String REST_URL = "/api/admin/users";

    public AdminUserController(UserRepository repository) {
        super(repository);
    }

    @Override
    @GetMapping
    @Operation(summary = "To get all users", description = "Returns all users' data, order by name and email")
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "To get user (by ID)", description = "Returns user's data by his ID")
    @ApiResponse(responseCode = "200", description = "User is found")
    @ApiResponse(responseCode = "404", description = "User is not found")
    public User get(@Parameter(description = "User's ID") @PathVariable long id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "users", key = "#user.email")
    @Operation(summary = "To create user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User is created")
    public ResponseEntity<User> createWithLocation(@Parameter(description = "User's data") @Valid @RequestBody User user) {
        return super.createWithLocation(user, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", allEntries = true)
    @Operation(summary = "To delete user", description = "Deletes a user by his ID")
    @ApiResponse(responseCode = "204", description = "User is deleted")
    @ApiResponse(responseCode = "404", description = "User is not found")
    public void delete(@Parameter(description = "User's ID") @PathVariable long id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#user.email")
    @Operation(summary = "To update user", description = "Updates a user by its ID")
    @ApiResponse(responseCode = "204", description = "User is updated")
    @ApiResponse(responseCode = "404", description = "User is not found")
    public void update(@Parameter(description = "User's data") @Valid @RequestBody User user,
                       @Parameter(description = "User's ID") @PathVariable long id) {
        super.update(user, id);
    }

    @GetMapping("/by-email")
    @Operation(summary = "To get user (by email)", description = "Returns user's data by his email")
    @ApiResponse(responseCode = "200", description = "User is found")
    @ApiResponse(responseCode = "404", description = "User is not found")
    public User getByEmail(@Parameter(description = "User's email") @RequestParam String email) {
        log.info("getByEmail {}", email);
        return repository.getExistedByEmail(email);
    }

    @GetMapping("/by-containing-name")
    @Operation(summary = "To get users by containing name",
            description = "Returns all users, whose names contain name form request parameter, order by name and email")
    public List<User> getByContainingName(@Parameter(description = "Name to contain") @RequestParam String name) {
        return super.getByContainingName(name, SORT);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "To set user enable/disable",
            description = "Enables/disables a user depending request parameter: true = enabled, false = disabled")
    @ApiResponse(responseCode = "204", description = "User is disabled/enabled")
    @ApiResponse(responseCode = "404", description = "User is not found")
    public void enable(@Parameter(description = "User's ID") @PathVariable long id,
                       @Parameter(description = "Boolean value") @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
    }
}