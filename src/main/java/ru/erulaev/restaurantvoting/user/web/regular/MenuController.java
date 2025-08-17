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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.service.DateService;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;
import ru.erulaev.restaurantvoting.user.web.response.CommonRegularApiResponses;
import ru.erulaev.restaurantvoting.user.web.response.schema.ProblemDetailSchema;

import java.time.LocalDate;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Menu controller", description = "Getting menu with dishes")
@CommonRegularApiResponses
public class MenuController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    private final MenuService menuService;
    private final DateService dateService;

    @GetMapping
    @Operation(summary = "To get menu", description = "Returns menu's data (menu's ID, date, restaurant's ID, list of dishes) " +
            "by restaurant's ID. By default menu is for current date. You can get menu for date specified at request parameter")
    @ApiResponse(responseCode = "200", description = "Menu is found")
    @ApiResponse(responseCode = "400", description = "Wrong date format",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "404", description = "Restaurant is not found, or no available menu for requested date",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public ResponseEntity<RegularMenuTo> getByDate(@Parameter(description = "Restaurant's ID") @PathVariable long restaurantId,
                                                   @Parameter(description = "Date, format - yyyy-MM-dd") @RequestParam(required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        date = dateService.processDate(date);
        log.info("get for restaurant {} by date {}", restaurantId, date);
        return ResponseEntity.of(menuService.getByDate(restaurantId, date));
    }
}
