package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;

import java.time.Instant;

@NamedQueries({
        @NamedQuery(name = Restaurant.GET_ALL, query = "SELECT r From Restaurant r ORDER BY r.name")
})
@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "uk_restaurant_name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Restaurant extends NamedEntity {

    static final String GET_ALL = "Restaurant.getAll";

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant registered = Instant.now();
}