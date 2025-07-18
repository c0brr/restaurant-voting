package ru.erulaev.restaurantvoting.user.to.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.erulaev.restaurantvoting.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class RequestVoteTo extends BaseTo {

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate date = LocalDate.now();

    @Min(1)
    @NotNull
    long restaurantId;

    @Override
    public String toString() {
        return "RequestVoteTo:" + id;
    }
}