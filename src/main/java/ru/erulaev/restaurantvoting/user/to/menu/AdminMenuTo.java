package ru.erulaev.restaurantvoting.user.to.menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AdminMenuTo extends BaseTo {

    protected LocalDate creationDate;

    protected int restaurantId;

    @Override
    public String toString() {
        return "AdminMenuTo:" + id;
    }
}