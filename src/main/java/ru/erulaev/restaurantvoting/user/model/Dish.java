package ru.erulaev.restaurantvoting.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;

@Entity
@Table(name = "dish", uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "name"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 100000)
    @NotNull
    private int price;

    @ManyToOne
    private Menu menu;

    public Dish(String name, int price) {
        super(name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish:" + id;
    }
}
