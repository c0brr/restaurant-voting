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

    @Column(name = "created", nullable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant created = Instant.now();

    public Restaurant(Restaurant restaurant) {
        this(restaurant.id, restaurant.name, restaurant.created);
    }

    public Restaurant(Integer id, String name, Instant created) {
        this(id, name);
        this.created = created;
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}