package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;
import ru.erulaev.restaurantvoting.user.util.DateUtil;

import java.time.LocalDate;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Regular menu controller", description = "Getting menu with dishes")
public class MenuController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "To get menu", description = "Returns menu with its dishes by restaurant's ID. " +
            "By default - for current date, or by date specified at request parameter")
    @ApiResponse(responseCode = "200", description = "Menu is found")
    @ApiResponse(responseCode = "404", description = "Menu or restaurant is not found")
    public ResponseEntity<RegularMenuTo> getByDate(@Parameter(description = "Restaurant's ID") @PathVariable long restaurantId,
                                                   @Parameter(description = "Date") @RequestParam(required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DateUtil.processDate(date);
        log.info("get for restaurant {} by date {}", restaurantId, date);
        return ResponseEntity.of(menuService.getByDate(restaurantId, date));
    }
}
