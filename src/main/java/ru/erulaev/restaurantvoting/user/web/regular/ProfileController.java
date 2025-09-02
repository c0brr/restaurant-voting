package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.mapper.UserMapper;
import ru.erulaev.restaurantvoting.user.entity.User;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;
import ru.erulaev.restaurantvoting.user.to.UserTo;
import ru.erulaev.restaurantvoting.user.web.swagger.BodyAndDataApiResponses;
import ru.erulaev.restaurantvoting.user.web.swagger.schema.ProblemDetailSchema;

import java.net.URI;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Profile controller", description = "User's profile management")
@ApiResponse(responseCode = "500", description = "Iternal Server Error",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
public class ProfileController {

    static final String REST_URL = "/api/profile";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "To get user", description = "Returns user's data (ID, name, email, enable status, registered date, roles)" +
            " by his authentication")
    @ApiResponse(responseCode = "200", description = "User is found")
    @ApiResponse(responseCode = "401", description = "Authorization required for this request",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#authUser.username")
    @Operation(summary = "To delete user",
            description = "Deletes a user by his authentication. User's roles will also be deleted. User's votes will remain for voting history")
    @ApiResponse(responseCode = "204", description = "User is deleted")
    @ApiResponse(responseCode = "401", description = "Authorization required for this request",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "404", description = "User is not found")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        userRepository.deleteExisted(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @CacheEvict(value = "users", key = "#userTo.email")
    @Operation(summary = "To register user", description = "Registers a new user")
    @ApiResponse(responseCode = "201", description = "User is registered")
    @ApiResponse(responseCode = "403", description = "You must be not authenticated for this request",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @BodyAndDataApiResponses
    public ResponseEntity<User> register(@Parameter(description = "User's data (name, email, password)")
                                         @Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = userRepository.prepareAndSave(userMapper.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#authUser.username")
    @Transactional
    @Operation(summary = "To update user", description = "Updates a user by his authentication")
    @ApiResponse(responseCode = "204", description = "User is updated")
    @ApiResponse(responseCode = "404", description = "User is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "401", description = "Authorization required for this request",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @BodyAndDataApiResponses
    public void update(@Parameter(description = "User's data (ID (not required), name, email, password)")
                       @Valid @RequestBody UserTo userTo,
                       @AuthenticationPrincipal AuthUser authUser) {
        int id = authUser.id();
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        User user = authUser.getUser();
        userRepository.prepareAndSave(userMapper.updateFromTo(user, userTo));
    }
}