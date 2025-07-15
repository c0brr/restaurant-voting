package ru.erulaev.restaurantvoting.user.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate date;

    long restaurantId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<DishTo> dishes;

    public MenuTo(Long id, LocalDate date, long restaurantId, List<DishTo> dishes) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }

    public MenuTo(Long id, LocalDate date, long restaurantId) {
        this(id, date, restaurantId, null);
    }

    @Override
    public String toString() {
        return "MenuTo:" + id;
    }
}