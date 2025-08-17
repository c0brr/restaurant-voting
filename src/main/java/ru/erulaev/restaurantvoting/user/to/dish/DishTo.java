package ru.erulaev.restaurantvoting.user.to.dish;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.NamedTo;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    protected int price;

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']';
    }
}
