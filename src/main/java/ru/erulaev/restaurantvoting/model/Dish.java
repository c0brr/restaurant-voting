package ru.erulaev.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "dish")
@NoArgsConstructor
@Getter
@Setter
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 100000)
    @NotNull
    private int price;

    @ManyToOne
    private Menu menu;

    public Dish(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Dish:" + id;
    }
}
