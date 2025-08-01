package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import ru.erulaev.restaurantvoting.user.web.apiResponse.CommonRegularApiResponses;
import ru.erulaev.restaurantvoting.user.web.apiResponse.SearchResultApiResponses;
import ru.erulaev.restaurantvoting.user.web.apiResponse.schema.ProblemDetailSchema;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Restaurant controller", description = "Getting restaurants with their vote count")
@CommonRegularApiResponses
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "To get all restaurants",
            description = "Returns all restaurants' data (ID, name, created date, vote count). " +
                    "By default, for current date. You can request restaurants with their votes for date specified " +
                    "at request parameter. Shows restaurants that exist for requested date")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Wrong date format",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "404", description = "No data available for requested date",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public List<RestaurantTo> getAllByDate(@Parameter(description = "Date, format - yyyy-MM-dd")
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        date = DateUtil.processDate(date);
        log.info("getAll by date {}", date);
        return restaurantService.getAllByDate(date);
    }

    @GetMapping("/{id}")
    @Operation(summary = "To get restaurant (by ID)",
            description = "Returns restaurant's data (ID, name, created date, vote count for today) by its ID")
    @SearchResultApiResponses
    public RestaurantTo get(@Parameter(description = "Restaurant's ID") @PathVariable long id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "To get restaurant (by name)",
            description = "Returns restaurant's data (ID, name, created date, vote count for today) by its name")
    @SearchResultApiResponses
    public RestaurantTo getByName(@Parameter(description = "Restaurant's name") @RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }
}