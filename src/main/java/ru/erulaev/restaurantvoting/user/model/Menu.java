package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.model.BaseEntity;

import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Menu.GET_ALL, query =
                "SELECT m FROM Menu m WHERE m.parentEntity.id = :restaurantId ORDER BY m.created DESC")
})
@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "created"}, name = "uk_restaurant_menu_created"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Menu extends BaseEntity {

    public static final String GET_ALL = "Menu.getAllByRestaurantId";

    @Column(name = "created", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate created = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant parentEntity;
}