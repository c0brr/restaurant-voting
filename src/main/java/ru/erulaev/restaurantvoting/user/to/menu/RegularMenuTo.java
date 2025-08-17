package ru.erulaev.restaurantvoting.user.to.menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.user.to.dish.DishTo;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = "dishTos")
public class RegularMenuTo extends AdminMenuTo {

    private List<DishTo> dishTos;

    @Override
    public String toString() {
        return "RegularMenuTo:" + id;
    }
}
