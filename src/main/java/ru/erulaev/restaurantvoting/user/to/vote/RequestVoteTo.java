package ru.erulaev.restaurantvoting.user.to.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.Clock;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequestVoteTo extends BaseTo {

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date = LocalDate.now(Clock.systemUTC());

    @Min(1)
    private long restaurantId;

    public RequestVoteTo(long restaurantId, LocalDate date) {
        this(restaurantId);
        this.date = date;
    }

    public RequestVoteTo(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "RequestVoteTo:" + id;
    }
}