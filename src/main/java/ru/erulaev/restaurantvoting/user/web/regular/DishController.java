package ru.erulaev.restaurantvoting.user.web.regular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erulaev.restaurantvoting.user.service.DishService;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class DishController {

    static final String REST_URL = "/api/menus/{menuId}/dishes";

    private final DishService dishService;

    @GetMapping
    public List<DishTo> getAll(@PathVariable long menuId) {
        log.info("getAll for menu {}", menuId);
        return dishService.getAll(menuId);
    }
}