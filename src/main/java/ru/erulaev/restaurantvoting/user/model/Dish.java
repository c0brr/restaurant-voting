package ru.erulaev.restaurantvoting.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;

@NamedQueries({
        @NamedQuery(name = Dish.GET_ALL_BY_MENU, query =
                "SELECT d FROM Dish d WHERE d.parentEntity.id = :menuId ORDER BY d.name ASC"),
        @NamedQuery(name = Dish.GET_BY_MENU_AND_NAME, query =
                "SELECT d FROM Dish d WHERE d.parentEntity.id = :menuId AND d.name = :name")
})
@Entity
@Table(name = "dish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "name"}, name = "uk_menu_dish_name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Dish extends NamedEntity {

    static final String GET_ALL_BY_MENU = "Dish.getAllByMenuId";
    static final String GET_BY_MENU_AND_NAME = "Dish.getByMenuIdAndName";

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 100000)
    @NotNull
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Menu parentEntity;

    @Schema(hidden = true)
    public Long getMenuId() {
        return parentEntity.getId();
    }
}