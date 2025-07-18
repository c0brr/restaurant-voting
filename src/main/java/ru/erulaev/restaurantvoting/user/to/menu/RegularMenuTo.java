package ru.erulaev.restaurantvoting.user.to.menu;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RegularMenuTo extends MenuTo {

    List<DishTo> dishes;

    public RegularMenuTo(Long id, LocalDate date, long restaurantId, List<DishTo> dishes) {
        super(id, date, restaurantId);
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "RegularMenuTo:" + id;
    }
}
