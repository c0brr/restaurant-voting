package ru.erulaev.restaurantvoting.user.to.dish;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DishToWithMenuId extends DishTo {

    private int menuId;

    @Override
    public String toString() {
        return "DishToWithMenuId:" + id + '[' + name + ']';
    }
}