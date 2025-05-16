package ru.erulaev.restaurantvoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Restaurant extends AbstractNamedEntity {

    @OneToMany
    private List<Menu> menus;

    @OneToMany
    private Set<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id, name);
        this.name = name;

    }

    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
