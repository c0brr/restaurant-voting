package ru.erulaev.restaurantvoting.user.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class ResponseVoteTo extends BaseTo implements HasId {

    LocalDate created;

    long restaurantId;

    long userId;

    public ResponseVoteTo(Long id, LocalDate created, long restaurantId, long userId) {
        super(id);
        this.created = created;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ResponseVoteTo:" + id;
    }
}