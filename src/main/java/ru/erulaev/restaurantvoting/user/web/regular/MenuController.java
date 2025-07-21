package ru.erulaev.restaurantvoting.user.web.regular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.service.MenuService;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;

import java.time.LocalDate;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class MenuController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<RegularMenuTo> getByDate(@PathVariable long restaurantId,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        date = date != null ? date : LocalDate.now();
        log.info("get for restaurant {} by date {}", restaurantId, date);
        return ResponseEntity.of(menuService.getByDate(restaurantId, date));
    }
}
