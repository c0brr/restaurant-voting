package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.to.NamedTo;

import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    Date registered;

    int votes;

    public RestaurantTo(Long id, String name, Date registered, int votes) {
        super(id, name);
        this.registered = registered;
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + name + ']';
    }
}