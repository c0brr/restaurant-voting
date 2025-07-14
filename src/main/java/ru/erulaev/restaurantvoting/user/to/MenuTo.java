package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate date;

    long restaurantId;

    public MenuTo(Long id, LocalDate date, long restaurantId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "MenuTo:" + id;
    }
}