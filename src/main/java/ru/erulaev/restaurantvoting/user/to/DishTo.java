package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.NamedTo;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    private int price;

    private int menuId;

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']';
    }
}