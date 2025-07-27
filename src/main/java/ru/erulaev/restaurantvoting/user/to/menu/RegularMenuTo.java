package ru.erulaev.restaurantvoting.user.to.menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.user.to.DishTo;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RegularMenuTo extends AdminMenuTo {

    private List<DishTo> dishes;

    @Override
    public String toString() {
        return "RegularMenuTo:" + id;
    }
}
