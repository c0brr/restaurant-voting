package ru.erulaev.restaurantvoting.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.entity.BaseEntity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Menu.GET_ALL_BY_RESTAURANT, query =
                "SELECT m FROM Menu m WHERE m.parentEntity.id = :restaurantId ORDER BY m.creationDate DESC"),
        @NamedQuery(name = Menu.GET_WITH_DISHES_BY_DATE, query =
                "SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.parentEntity.id = :restaurantId AND m.creationDate = :creationDate"),
        @NamedQuery(name = Menu.GET_BY_RESTAURANT_AND_DATE, query =
                "SELECT m FROM Menu m WHERE m.parentEntity.id = :restaurantId AND m.creationDate = :creationDate")
})
@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "creation_date"}, name = "uk_restaurant_menu_date"))
@NoArgsConstructor
@Getter
@Setter
public class Menu extends BaseEntity {

    static final String GET_ALL_BY_RESTAURANT = "Menu.getAllByRestaurantId";
    static final String GET_WITH_DISHES_BY_DATE = "Menu.getWithDishesByCreationDate";
    static final String GET_BY_RESTAURANT_AND_DATE = "Menu.getByRestaurantIdAndCreationDate";

    @Column(name = "creation_date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate creationDate = LocalDate.now(Clock.systemUTC());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Restaurant parentEntity;

    @OneToMany(mappedBy = "parentEntity")
    @OrderBy("name ASC")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Dish> dishes;

    public Menu(Integer id, LocalDate creationDate, Restaurant parentEntity) {
        this(id, creationDate);
        this.parentEntity = parentEntity;
    }

    public Menu(Integer id, LocalDate creationDate) {
        super(id);
        this.creationDate = creationDate;
    }

    @Schema(hidden = true)
    public Integer getRestaurantId() {
        return parentEntity.getId();
    }
}