package ru.erulaev.restaurantvoting.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Restaurant extends NamedEntity {

    @OneToMany
    @JoinColumn(name = "restaurant_id")
    private List<Menu> menus;

    @OneToMany
    @JoinColumn(name = "restaurant_id")
    private Set<Vote> votes;

    public Restaurant(String name) {
        super(name);
        this.name = name;
    }

    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
