package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.model.BaseEntity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Menu.GET_ALL_BY_RESTAURANT, query =
                "SELECT m FROM Menu m WHERE m.parentEntity.id = :restaurantId ORDER BY m.date DESC"),
        @NamedQuery(name = Menu.GET_WITH_DISHES_BY_DATE, query =
                "SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.parentEntity.id = :restaurantId AND m.date = :date"),
        @NamedQuery(name = Menu.GET_BY_RESTAURANT_AND_DATE, query =
                "SELECT m FROM Menu m WHERE m.parentEntity.id = :restaurantId AND m.date = :date")
})
@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "date"}, name = "uk_restaurant_menu_date"))
@NoArgsConstructor
@Getter
@Setter
public class Menu extends BaseEntity {

    static final String GET_ALL_BY_RESTAURANT = "Menu.getAllByRestaurantId";
    static final String GET_WITH_DISHES_BY_DATE = "Menu.getWithDishesByDate";
    static final String GET_BY_RESTAURANT_AND_DATE = "Menu.getByRestaurantIdAndDate";

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date = LocalDate.now(Clock.systemUTC());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Restaurant parentEntity;

    @OneToMany(mappedBy = "parentEntity")
    @OrderBy("name ASC")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Dish> dishes;

    public Menu(Long id, LocalDate date, Restaurant parentEntity) {
        this(id, date);
        this.parentEntity = parentEntity;
    }

    public Menu(Long id, LocalDate date) {
        super(id);
        this.date = date;
    }

    @Schema(hidden = true)
    public Long getRestaurantId() {
        return parentEntity.getId();
    }
}