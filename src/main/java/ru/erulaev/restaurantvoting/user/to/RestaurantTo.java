package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.NamedTo;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    private Date registered;

    private int votes;

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + name + ']';
    }
}