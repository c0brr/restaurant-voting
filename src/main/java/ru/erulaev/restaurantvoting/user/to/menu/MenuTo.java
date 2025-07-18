package ru.erulaev.restaurantvoting.user.to.menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    protected LocalDate date;

    protected long restaurantId;

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