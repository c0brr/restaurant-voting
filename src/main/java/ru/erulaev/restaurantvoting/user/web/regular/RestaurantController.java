package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.service.RestaurantService;
import ru.erulaev.restaurantvoting.user.to.RestaurantTo;
import ru.erulaev.restaurantvoting.user.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Regular restaurant controller",
        description = "Getting restaurants with their current vote count for today")
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "To get all restaurants",
            description = "Returns all restaurants with their vote counts. " +
                    "By default - for current date, or by date specified at request parameter")
    @ApiResponse(responseCode = "409", description = "No data for requested date")
    public List<RestaurantTo> getAllByDate(@Parameter(description = "Date") @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DateUtil.processDate(date);
        log.info("getAll by date {}", date);
        return restaurantService.getAllByDate(date);
    }

    @GetMapping("/{id}")
    @Operation(summary = "To get restaurant (by ID)", description = "Returns restaurant with its vote count by its ID")
    @ApiResponse(responseCode = "200", description = "Restaurant is found")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found")
    public RestaurantTo get(@Parameter(description = "Restaurant's ID") @PathVariable long id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "To get restaurant (by name)", description = "Returns restaurant with its count votes by its name")
    @ApiResponse(responseCode = "200", description = "Restaurant is found")
    @ApiResponse(responseCode = "404", description = "Restaurant is not found")
    public RestaurantTo getByName(@Parameter(description = "Restaurant's name") @RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }
}