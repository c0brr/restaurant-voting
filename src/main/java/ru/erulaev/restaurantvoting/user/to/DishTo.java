package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo implements HasId {

    int price;

    long menuId;

    public DishTo(Long id, String name, int price, long menuId) {
        super(id, name);
        this.price = price;
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']';
    }
}