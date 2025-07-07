package ru.erulaev.restaurantvoting.user.model;

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
        @NamedQuery(name = Dish.GET_ALL, query =
                "SELECT d FROM Dish d WHERE d.menu.id = :menuId ORDER BY d.name ASC"),
        @NamedQuery(name = Dish.GET, query =
                "SELECT d FROM Dish d WHERE d.id = :id AND d.menu.id = :menuId"),
        @NamedQuery(name = Dish.DELETE, query =
                "DELETE FROM Dish d WHERE d.id = :id AND d.menu.id = :menuId"),
        @NamedQuery(name = Dish.GET_BY_NAME, query =
                "SELECT d FROM Dish d WHERE d.name = LOWER(:name) AND d.menu.id = :menuId")
})
@Entity
@Table(name = "dish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "name"}, name = "uk_menu_dish_name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Dish extends NamedEntity {

    public static final String GET_ALL = "Dish.getAllByMenuId";
    public static final String GET = "Dish.get";
    public static final String DELETE = "Dish.delete";
    public static final String GET_BY_NAME = "Dish.getByNameIgnoreCaseAndMenuId";

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 100000)
    @NotNull
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;

    @Override
    public String toString() {
        return "Dish:" + id;
    }
}