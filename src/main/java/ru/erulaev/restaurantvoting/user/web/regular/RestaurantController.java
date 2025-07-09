package ru.erulaev.restaurantvoting.user.web.regular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.service.RestaurantService;
import ru.erulaev.restaurantvoting.user.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll for today");
        return restaurantService.getAll(LocalDate.now());
    }

    @GetMapping("?date")
    public List<RestaurantTo> getAllByDate(@RequestParam LocalDate date) {
        log.info("getAll for date {}", date);
        return restaurantService.getAll(date);
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable long id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/by-name")
    public RestaurantTo getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }
}